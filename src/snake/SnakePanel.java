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
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SnakePanel extends JPanel implements ActionListener
{
    private Snake snake;
    
    private final int WIN_WIDTH = 500;
    private final int WIN_HEIGHT = 500;
    private final int SEG_SIZE = 25;
    private final int BORDER_SIZE = 1;
    private final int DELAY = 120;
    private final int START_LENGTH = 5;
    private final int FOOD_DELAY = 50;

    private int xDirection, yDirection, x, y, foodClock, score;
    private boolean paused, foodTime, dead;
    private Timer clock;
    private Segment food;
    private SnakeFrame frame;

    public final void init(SnakeFrame f)
    {
        frame = f;
        score = 0;
        xDirection = 1;
        yDirection = 0;
        paused = false;
        dead = false;
        foodClock = 0;
        x = (WIN_WIDTH / SEG_SIZE) / 2 - START_LENGTH;
        y = (WIN_HEIGHT / SEG_SIZE) / 2;
        
        snake = new Snake(SEG_SIZE, BORDER_SIZE);
        snake.startAt(x, y, START_LENGTH);
        
        x = (snake.head().x - BORDER_SIZE) / SEG_SIZE;
        y = (snake.head().y - BORDER_SIZE) / SEG_SIZE;

        setBackground(Color.black);
        setVisible(true);
        setPreferredSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        clock = new Timer(DELAY, this);
        move();
        clock.start();
        updateScore();
    }

    public void actionPerformed(ActionEvent event)
    {
        if (dead) {
            clock.stop();
        }
        else if (!paused) {
            foodClock++;
            if (foodClock >= FOOD_DELAY) {
                foodTime = true;
                foodClock = 0;
            }
            move();
        }
        paintComponent(getGraphics());
    }

    private void move()
    {
        x += xDirection;
        y += yDirection;
        Segment head = snake.push(x, y);

        dead = isDead();
        if (dead) {
            return;
        }
        
        if (x < 0 || (x * SEG_SIZE) >= WIN_WIDTH
                || y < 0 || (y * SEG_SIZE) >= WIN_HEIGHT) {
            dead = true;
            return;
        }

        if (head.intersects(food)) {
            food = null;
            foodTime = false;
            score += 5;
            updateScore();
        }
        else {
            snake.pop();
        }
    }

    private void updateScore()
    {
        frame.setTitle("SNAKE - Score: " + score);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (dead) {
            setBackground(Color.white);
            if (food != null) {
                g.setColor(Color.yellow);
                food.paint(g);
            }
            snake.setColor(Color.black);
            g.setColor(Color.red);
            g.drawString("You died!", (WIN_WIDTH - 45) / 2, (WIN_HEIGHT - 100) / 2);
        }
        draw(g);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    private void draw(Graphics g)
    {
        snake.paint(g);
        drawFood(g);
    }

    private void drawFood(Graphics g)
    {
        if (food == null) {
            if (foodTime) {
                int foodX = (int) (Math.random() * (WIN_WIDTH / SEG_SIZE));
                int foodY = (int) (Math.random() * (WIN_HEIGHT / SEG_SIZE));
                food = new Segment((foodX * SEG_SIZE) + BORDER_SIZE, (foodY * SEG_SIZE) + BORDER_SIZE, SEG_SIZE - BORDER_SIZE, SEG_SIZE - BORDER_SIZE);
                g.setColor(Color.red);
                food.paint(g);
            }
        }
        else {
            g.setColor(Color.red);
            food.paint(g);
        }
    }

    public void setXDirection(int xDir)
    {
        xDirection = xDir;
        yDirection = 0;
    }

    public void setYDirection(int yDir)
    {
        yDirection = yDir;
        xDirection = 0;
    }

    public int getXDirection()
    {
        return xDirection;
    }

    public int getYDirection()
    {
        return yDirection;
    }

    public boolean isDead()
    {
        return dead;
    }

    public void togglePause()
    {
        paused = !paused;
    }

    public boolean isPaused()
    {
        return paused;
    }
}
