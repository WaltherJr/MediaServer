package org.eriksandsten.devtools;

record ChromeConnection(MediaServerWebSocketClient webSocketClient, BrowserTarget browserTarget) {}
