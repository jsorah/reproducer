# Steps to reproduce:

1. Create some certificates (e.g. selfsigned or using mkcert)
   * `openssl req -newkey rsa:2048 -nodes -keyout private_key.pem -x509 -days 365 -out self_signed_certificate.pem`
2. set them in quarkus.properties in the specific fields 
3. Build & run - quarkus should be available at localhost:8443 (if not, change go client address in gist below)
4. Use python3 and create a virtual environment in a new directory
   * `cd src/main/python`
   * `python -m venv env`
   * `source env/bin/activate`
   * `pip install certifi h2`
5. run `python empty_frame.py` from your python virtual env
6. See bytes written out in reproducer server, and response message indicates `0x00` was detected 
7. comment lines in `empty_frame.py` as mentioned to not send the empty frame.
8. see happy response

# reproducer Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .