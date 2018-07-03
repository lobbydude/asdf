package com.hybrid.framework.utility;

import com.hybrid.framework.config.Constants;
import com.hybrid.framework.config.DriverActions;

import java.io.IOException;

public class BrowserSelector {
    public static void invokeBrowser(int runMode) throws IOException {
        if (runMode == 2) {
            DriverActions.openBrowser("", "", Constants.CHROME);
        }
        else if (runMode == 5) {
            DriverActions.openBrowser("", "", Constants.FIREFOX);
        }
    }
}
