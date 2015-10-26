/* 
 * The MIT License
 *
 * Copyright 2015 Timothy Baxendale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package snake;

/**
 *
 * @author Timothy Baxendale
 */
import javax.swing.*;
import java.awt.event.*;

public class SnakeFrame extends JFrame
        implements KeyListener
{

    private final SnakePanel panel;

    public SnakeFrame()
    {
        super("SNAKE");
        panel = new SnakePanel();
    }

    public void init()
    {
        addKeyListener(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel);
        panel.init(this);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public void keyPressed(KeyEvent ev)
    {
        int key = ev.getKeyCode();
        if (key == 32) {
            if (panel.isDead()) {
                panel.init(this);
            }
            else {
                panel.togglePause();
            }
        }
        if (key < 37 || key > 40 || panel.isPaused()) {
            return;
        }
        int xDirection = panel.getXDirection(),
            yDirection = panel.getYDirection();

        if (xDirection != 1 && key == 37) {
            panel.setXDirection(-1);
        }
        else if (xDirection != -1 && key == 39) {
            panel.setXDirection(1);
        }
        else if (yDirection != 1 && key == 38) {
            panel.setYDirection(-1);
        }
        else if (yDirection != -1 && key == 40) {
            panel.setYDirection(1);
        }
    }

    public void keyTyped(KeyEvent ev)
    {
    }

    public void keyReleased(KeyEvent ev)
    {
    }
}
