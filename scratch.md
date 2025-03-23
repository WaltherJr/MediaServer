chrome://settings/content/automaticFullScreen

`javac -h . C:\Users\eriks\Desktop\MediaServer\src\main\java\org\eriksandsten\utils\Win32Helper.java`

`g++ -static -shared -Wl,--kill-at -fPIC -o win32helper.dll -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32" -I"C:\mingw64\include\c++\14.2.0" JniUtils.cpp WindowFinder.cpp`
`g++ -static -o Test.exe -I"%JAVA_HOME%/include/win32" -I"C:\mingw64\include\c++\14.2.0" Test.cpp`

https://www.zigbee2mqtt.io/advanced/zigbee/04_sniff_zigbee_traffic.html

Some YouTube video IDs:

| Video ID    | Song                                 |
| ----------- | ------------------------------------ |
| 7NrQei36fJk | Westlife - If I Let You Go           |
| 4fndeDfaWCg | Backstreet Boys - I Want It That Way |

```
public String run(String startUrl) throws IOException, URISyntaxException, InterruptedException {
    MediaServerWebSocketClient webSocketClient = connectToChrome();
    GetTargetsResponse getTargetsCommand = (GetTargetsResponse) webSocketClient.executeCommand(new GetTargetsRequest(), GetTargetsResponse.class);

    Optional<GetTargetsResponse.TargetInfo> existingChannelTab = getTargetsCommand.result.targetInfos.stream().filter(target -> target.type.equals(TargetType.PAGE.getType()) && target.title.equals(mediaServerPageChannelTitle)).findFirst();
    final String targetId = existingChannelTab.map(targetInfo -> targetInfo.targetId).orElseGet(() -> {
        CreateTargetResponse createTargetResponse = (CreateTargetResponse) webSocketClient.executeCommand(new CreateTargetRequest(startUrl), CreateTargetResponse.class);
        return createTargetResponse.result.targetId;
    });

    AttachToTargetResponse attachToTargetResponse = (AttachToTargetResponse) webSocketClient.executeCommand(new AttachToTargetRequest(targetId, true), AttachToTargetResponse.class);
    final String sessionId = attachToTargetResponse.result.sessionId;

    EnableResponse enableResponse = (EnableResponse) webSocketClient.executeCommand(new EnableRequest(sessionId), EnableResponse.class);

    webSocketClient.addCallback("Page.loadEventFired", (params) -> {
        RuntimeEvaluateResponse evaluateResponse2 = (RuntimeEvaluateResponse) webSocketClient.executeCommand(
                new EvaluateRequest(sessionId, jsSnippet.formatted(mediaServerPageChannelTitle)), RuntimeEvaluateResponse.class);
/*
        try {
            Thread.sleep(5000);
            DispatchKeyEventResponse dispatchKeyEventResponse = (DispatchKeyEventResponse) webSocketClient.executeCommand(
                    new DispatchKeyEventRequest(sessionId, "char", "f"), DispatchKeyEventResponse.class
            );
            System.out.println(dispatchKeyEventResponse);
        } catch (InterruptedException e) {

        }*/
    });

    // TODO: a lot of responses after issuing command, need to wait for these in the "onMessage" method!
    //GetDocumentResponse command4 = (GetDocumentResponse) webSocketClient.executeCommand(new GetDocumentRequest(sessionId, -1, true), GetDocumentResponse.class);

    //var htmlNodes = command4.result.root.children;
    //var htmlNode2 = htmlNodes.get(1);
    //var bodyNode = htmlNode2.children.get(1);

    return "HEJSAN!";
}
```
```
// TODO: a lot of responses after issuing command, need to wait for these in the "onMessage" method!
//GetDocumentResponse command4 = (GetDocumentResponse) webSocketClient.executeCommand(new GetDocumentRequest(sessionId, -1, true), GetDocumentResponse.class);

//var htmlNodes = command4.result.root.children;
//var htmlNode2 = htmlNodes.get(1);
//var bodyNode = htmlNode2.children.get(1);
```