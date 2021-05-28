package renderer.input;

import java.awt.event.*;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int mouseX = -1;
    private int mouseY = -1;
    private int scroll = 0;

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isScrollingUp() {
        return this.scroll == -1;
    }

    public boolean isScrollingDown() {
        return this.scroll == 1;
    }

    public void reset(){
        this.scroll = 0;
    }

    public ClickType getButton() {
        switch (this.mouseB) {
            case 1:
                return ClickType.LeftClick;
            case 2:
                return ClickType.ScrollClick;
            case 3:
                return ClickType.RightClick;
            case 4:
                return ClickType.Back;
            case 5:
                return ClickType.Front;
            default:
                return ClickType.Unknown;
        }
    }

    public void setMouseB(int mouseB) {
        this.mouseB = mouseB;
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }

    private int mouseB = -1;

    public void resetButton(){
        this.mouseB = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent event) {
        this.mouseB = event.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseB = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent event) {
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        this.mouseX = event.getX();
        this.mouseY = event.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        scroll = event.getWheelRotation();
    }
}
