# domain-socket-mre
MRE for domain-socket performance

Please compile the server and client:
```
mvn clean install
```
The repo comes with two benchmark scripts that each run for 1 minute
and print the moving average of the send latency via websocket
and domain socket, respectively.

The latency measurement is based on the duration between the timestamp generated
in the client and the server's receive timestamp.
The server logs the rolling average over the past 2000 messages.

The following runs websocket benchmark:
```
./websocket.sh
```

The following runs domain-socket benchmark:
```
./domainsocket.sh
```