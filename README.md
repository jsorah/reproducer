# Steps to reproduce:

1. Create some certificates (e.g. selfsigned or using mkcert)
2. set them in quarkus.properties in the specific fields 
3. Build& run - quarkus should be available at localhost:8443 (if not, change go client address in gist below)
4. download/install go 1.13 
5. Save the following go program in a file called `main.go` - https://gist.github.com/DGuhr/cb49cc77e39b2d009a4d36bdcd89c7dd inside a directory `go_client`
6. run `go build` in that directory
7. run `./go_client`
8. See exception in response. 
9. remove comment ( // ) from line 16 of main.go
10. see proper response


# reproducer Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .