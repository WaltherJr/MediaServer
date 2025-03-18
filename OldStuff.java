
public ActiveTVChannelInfoResponse getActiveTVChannelInfo() {
    // Matcher matcher = allenteTVChannelURLRegex.matcher(Optional.ofNullable(webDriver.getCurrentUrl()).orElse(""));

    if (matcher.find()) {
        try {
            // webDriver.findElement(allenteMediaPlayerPlayButtonSelector);
            return new ActiveTVChannelInfoResponse(matcher.group(1),  false);
        } catch (NoSuchElementException e) {
            return new ActiveTVChannelInfoResponse(matcher.group(1),  true);
        }
    } else {
        return null;
    }
}

public void setActiveChannelPlayState(SetActiveChannelPlayStateRequestBody setActiveChannelPlayStateRequestBody) {
    WebElement buttonToClick = null;

    try {
        buttonToClick = webDriver.findElement(allenteMediaPlayerPauseButtonSelector);
    } catch (NoSuchElementException e) {
        try {
            buttonToClick = webDriver.findElement(allenteMediaPlayerPlayButtonSelector);
        } catch (NoSuchElementException d) {
            // Could be playing ads etc. TODO: return 405 illegal state or similar
            return;
        }
    }

    System.out.println("CLICKING PLAY STATE BUTTON...");
    buttonToClick.click();
}

void performLoginOrSwitchChannel(Consumer<WebElement> playButtonFound, Consumer<WebElement> loginButtonFound) {
    // final WebDriverWait oneSecondWait = new WebDriverWait(webDriver, Duration.ofSeconds(1));
    WebElement playButton = null;
    WebElement loginButton = null;

    for (int i = 0; i <= 3; i++) {
        try {
            playButton = new WebDriverWait(webDriver, Duration.ofMillis(500)).until(ExpectedConditions.elementToBeClickable(allenteMediaPlayerPlayButtonSelector));
            break;
        } catch (TimeoutException e) {
            try {
                loginButton = new WebDriverWait(webDriver, Duration.ofMillis(500)).until(ExpectedConditions.elementToBeClickable(allenteUserLoginButtonSelector));
                break;
            } catch (TimeoutException _) {
            }
        }
    }

    if (playButton != null) {
        playButtonFound.accept(playButton);
    } else if (loginButton != null) {
        loginButtonFound.accept(loginButton);
    } else {
        // One of the above should always be visible
        System.err.println("Could not find play button or login button for the Allente TV client");
        throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

public static WebDriver startChromeWebDriver(String url, @NonNull List<String> chromeArguments) throws IOException {
    //var chromeDriverService = new ChromeDriverService.Builder()
    //        .usingDriverExecutable(new File("C:/chromedriver-win64/chromedriver"))
    //        .usingAnyFreePort()
    //        .build();
    var options = new ChromeOptions();
    chromeArguments.forEach(arg -> options.addArguments("--" + arg));
    // options.addArguments("--user-data-dir=C:/Users/eriks/AppData/Local/Google/Chrome/User Data");
    // options.addArguments("--profile-directory=Default");
    options.setExperimentalOption("prefs", Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false)); // Remove "Save this login" popup
    options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
    // options.setExperimentalOption("debuggerAddress", "localhost:9014");
    var driver = new ChromeDriver(options);
    driver.get(url);
    return driver;
}

public void turnOnTVChannel(String channelId) {
    allenteVideoPlayer.navigateToUrl(String.format(allenteTVChannelURL, channelId));
    WebDriverWait longWait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
    WebDriverWait shorterWait = new WebDriverWait(webDriver, Duration.ofSeconds(4));
    WebDriverWait adsLongWait = new WebDriverWait(webDriver, Duration.ofMinutes(2));

    // TODO: handle ads By.cssSelector("div.PlayerAds")
    performLoginOrSwitchChannel((playButton) -> {
        System.err.println("User already logged in and media player visible, clicking play button...");
        playButton.click();

    }, (loginButton) -> {
        System.err.println("Did not find already logged in user, performing login flow");
        loginButton.click();

        try {
            WebElement userNameInput = new WebDriverWait(webDriver, Duration.ofSeconds(1)).until(ExpectedConditions.elementToBeClickable(allenteUsernameInputFieldSelector));
            WebElement passwordInput = new WebDriverWait(webDriver, Duration.ofSeconds(1)).until(ExpectedConditions.elementToBeClickable(allentePasswordInputFieldSelector));
            userNameInput.sendKeys(allenteUsername);
            passwordInput.sendKeys(allentePassword);

            webDriver.findElement(By.cssSelector("button#nextButton")).click();
            AllenteHelper.acceptCookies(webDriver);

            // Sometimes when going from EPG to channel, we need to click play again
            // WebElement playButton = new WebDriverWait(webDriver, Duration.ofSeconds(3000)).until(ExpectedConditions.elementToBeClickable(allenteMediaPlayerPlayButtonSelector));
            // playButton.click();
            // MediaHelper.switchHDMIChannel(thisServiceHDMIChannel);

        } catch (TimeoutException e) {
            System.err.println(e.getMessage());
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    });
}

private boolean isYoutubeFullscreen(WebDriver webDriver) {
    var element = webDriver.findElement(By.cssSelector("html > body.no-scroll"));
    return element != null;
}