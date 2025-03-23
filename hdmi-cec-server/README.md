Start the HDMI-CEC server:
```
export FLASK_ENV=development && python3 -m flask run -p 8000 -h 0.0.0.0 [--debug]    (Unix)
set FLASK_ENV=development; python -m flask run -p 8000 -h 0.0.0.0 [--debug]          (Windows)
```

Startup script for automatic server start:
`konsole --noclose -e /bin/bash {path-to-source-files}/hdmi-cec-server/run_server.sh`

`systemctl list-unit-files --state=enabled`

TV:n är icke-konfigurerad om man får "Transmit from Unregistered to TV (15 to 0):". Man ska få "Transmit from Specific to TV (14 to 0):
STANDBY (0x36)
Sequence: 76 Tx Timestamp: 479.949s

Hitta lokal IP-adress: `hostname -I`
