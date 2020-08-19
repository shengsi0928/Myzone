package ncut.cs.wulin.myzone.login.data.ui.login;

import java.io.Serializable;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String userId;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String userId) {
        this.displayName = displayName;
        this.userId = userId;
    }

    String getDisplayName() {
        return displayName;
    }

    String getUserId()
    {
        return userId;
    }
}