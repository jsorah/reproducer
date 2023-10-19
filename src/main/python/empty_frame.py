#!/usr/bin/env python3

"""
empty_frame.py
~~~~~~~~~~~~~~~~~~~~~~~
Just enough code to send a GET request via h2 to an HTTP/2 server and receive a response body.
This is *not* a complete production-ready HTTP/2 client!
Borrowed from https://python-hyper.org/projects/hyper-h2/en/stable/plain-sockets-example.html  with modifications.
set up (requires using a reproducer app server)
python3.9 -m venv venv
venv/bin/activate
pip install certifi h2
python empty_frame.py
"""

import socket
import ssl
import certifi

import h2.connection
import h2.events


SERVER_NAME = '127.0.0.1'
SERVER_PORT = 8443

# generic socket and ssl configuration
socket.setdefaulttimeout(15)
ctx = ssl._create_unverified_context()
ctx.set_alpn_protocols(['h2'])

# open a socket to the server and initiate TLS/SSL
s = socket.create_connection((SERVER_NAME, SERVER_PORT))
s = ctx.wrap_socket(s, server_hostname=SERVER_NAME)

c = h2.connection.H2Connection()
c.initiate_connection()
s.sendall(c.data_to_send())

headers = [
    (':method', 'POST'),
    (':path', '/hello'),
    (':authority', SERVER_NAME),
    (':scheme', 'https'),
    ('content-type','application/x-www-form-urlencoded')
]
c.send_headers(1, headers, end_stream=False)
s.sendall(c.data_to_send())

c.send_data(1,"a=b".encode('utf-8'), end_stream=False)
s.sendall(c.data_to_send())

# To 'fix' - comment these two lines and change end_stream=True in the previous c.send_data call
c.send_data(1,b'', end_stream=True)
s.sendall(c.data_to_send())

body = b''
response_stream_ended = False
while not response_stream_ended:
    # read raw data from the socket
    data = s.recv(65536 * 1024)
    if not data:
        break

    # feed raw data into h2, and process resulting events
    events = c.receive_data(data)
    for event in events:
        print(event)
        if isinstance(event, h2.events.DataReceived):
            # update flow control so the server doesn't starve us
            c.acknowledge_received_data(event.flow_controlled_length, event.stream_id)
            # more response body data received
            body += event.data
        if isinstance(event, h2.events.StreamEnded):
            # response body completed, let's exit the loop
            response_stream_ended = True
            break
    # send any pending data to the server
    s.sendall(c.data_to_send())

print("Response fully received:")
print(body.decode())

# tell the server we are closing the h2 connection
c.close_connection()
s.sendall(c.data_to_send())

# close the socket
s.close()
