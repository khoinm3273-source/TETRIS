package src;

import src.DrawUI.MenuUI;
import src.DrawUI.OverlayUI;
import src.Manager.PlayManager;

public class MouseController {
    private MenuUI menu;
    private OverlayUI overlay;
    private PlayManager pm;
    public MouseController(MenuUI menu, OverlayUI overlay, PlayManager pm) {
        this.menu = menu;
        this.overlay = overlay;
        this.pm = pm;
    }

    public void click(int mx, int my) {
        GameState state = pm.getState();
        if (state == GameState.START_MENU) {
            int i = menu.getButtonIndex(mx, my);
            if (i == 0) pm.resetGame(); // Gọi reset và bắt đầu game mới
            if (i == 1) pm.setState(GameState.HOW_TO_PLAY);
            if (i == 2) System.exit(0);
        } 
        else if (state == GameState.GAME_OVER) {
            // Kiểm tra click vào nút Play Again trong OverlayUI
            if (overlay.playAgainBtn.contains(mx, my)) {
                pm.resetGame(); // Reset toàn bộ board và score
            }
            if (overlay.quitBtn.contains(mx, my)) {
                System.exit(0);
            }
        }
        else if (state == GameState.HOW_TO_PLAY) {
            if (overlay.backBtn.contains(mx, my)) {
                pm.setState(GameState.START_MENU);
            }
        }
    }
}