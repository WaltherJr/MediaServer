
Starta enkel HTTP server: `python -m http.server 8000`

```
export FLASK_APP=${namn_på_entrypoint-fil}
export FLASK_ENV=development
python3 -m flask run -p 8000 -h 0.0.0.0
export FLASK_APP=myserver && export FLASK_ENV=development && python3 -m flask run -p 8000 -h 0.0.0.0
sudo apt-get install chromium-driver
```

Hitta lokal IP-adress: `hostname -I`
