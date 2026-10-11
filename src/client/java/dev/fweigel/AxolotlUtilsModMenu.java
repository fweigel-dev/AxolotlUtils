package dev.fweigel;

import dev.fweigel.mobutils.core.client.compat.FweigelModMenuBase;
import dev.fweigel.ui.AxolotlUtilsScreen;

public class AxolotlUtilsModMenu extends FweigelModMenuBase {
    public AxolotlUtilsModMenu() {
        super(AxolotlUtilsScreen::new);
    }
}
