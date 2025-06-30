import turtle

from backpropagation import MLP
import random
import numpy as np


class Print:
    def __init__(self):
        self.trt = turtle.Turtle()
        self.trt.hideturtle()
        self.trt.penup()
        self.trt.goto(0, 350)
        self.trt.color("red")
        self.trt.shapesize(stretch_wid=4, stretch_len=1)
        self.trt.write("A: 0 | B: 0", align="Center", font=("Arial", 20))

    def write(self, A_score, B_score):
        self.trt.clear()
        self.trt.write(
            "A: {} | B: {}".format(A_score, B_score),
            align="Center",
            font=("Arial", 30),
        )


class Wall:
    def __init__(self, position):
        self.trt = turtle.Turtle()
        self.trt.shape("square")
        self.trt.color("red")
        self.trt.shapesize(stretch_wid=8, stretch_len=1)
        self.trt.penup()
        self.trt.goto(position, 0)

    def down(self):
        y = self.trt.ycor()
        y -= 1
        if y > -330:
            self.trt.sety(y)

    def up(self):
        y = self.trt.ycor()
        y += 1
        if y < 330:
            self.trt.sety(y)


class Ball:
    def __init__(self, a, b, print):
        self.trt = turtle.Turtle()
        self.trt.shape("circle")
        self.trt.color("red")
        self.dx = 0.5
        self.dy = 0.5
        self.trt.penup()
        self.trt.goto(0, 0)
        self.A_score = 0
        self.B_score = 0
        self.a = a
        self.b = b
        self.print = print


    def collisions(self):
        if self.trt.ycor() > 390:
            self.trt.sety(390)
            self.dy *= -1

        if self.trt.ycor() < -390:
            self.trt.sety(-390)
            self.dy *= -1

        if self.trt.xcor() > 490:
            self.trt.setx(490)
            self.dx *= -1
            self.A_score += 1
            self.print.write(self.A_score, self.B_score)
            if self.b.trt.ycor() > 0:
                self.trt.sety(-330)
            if self.b.trt.ycor() < 0:
                self.trt.sety(330)

        if self.trt.xcor() < -490:
            self.trt.setx(-490)
            self.dx *= -1
            self.B_score += 1
            self.print.write(self.A_score, self.B_score)
            if self.b.trt.ycor()>0:
                self.trt.sety(-330)
            if self.b.trt.ycor()<0:
                self.trt.sety(330)


        if (
            self.trt.xcor() < -430
            and abs(self.trt.ycor() - self.a.trt.ycor()) < 50
        ):
            self.trt.goto(-420, self.trt.ycor())
            self.dx *= -1

        if (
            self.trt.xcor() > 430
            and abs(self.trt.ycor() - self.b.trt.ycor()) < 50
        ):
            self.trt.goto(420, self.trt.ycor())
            self.dx *= -1

    def move(self):
        self.trt.setx(self.trt.xcor() + self.dx)
        self.trt.sety(self.trt.ycor() + self.dy)



class GameData:
    def __init__(self, ball_position, wall_position, upOrDown):
        self.wall_position = wall_position
        self.ball_position = ball_position
        self.upOrDown = upOrDown




def gra(window):
    a = Wall(-450)
    b = Wall(450)
    a.trt.shapesize(stretch_wid=1, stretch_len=1)
    d = Print()

    AI_type = 1

    en = 500

    #(self, n_input_nodes, hidden_nodes, n_output_nodes, lr):
    mlp = MLP(2, [2], 2, 0.0001)

    maxFrames = 20000
    currentFrame = 0

    gameDataList = []

    ball = Ball(a, b, d)
    set_keys(window, a, b)
    while True:
        window.update()
        ball.move()
        ball.collisions()

        if(AI_type == 1):
            if (b.trt.ycor()+4) > ball.trt.ycor():
                gameDataList.append(GameData((ball.trt.ycor()+360), (b.trt.ycor()+4+360), [0.1, 0.9]))
                b.down()
                if currentFrame < maxFrames:
                    currentFrame = currentFrame + 1
            elif (b.trt.ycor()+4) < ball.trt.ycor():
                gameDataList.append(GameData((ball.trt.ycor()+360), (b.trt.ycor()+4+360), [0.9, 0.1]))
                b.up()
                if currentFrame < maxFrames:
                    currentFrame = currentFrame + 1



            if currentFrame >= maxFrames:
                for e in range(en):
                    for r in range(9999):
                        n = random.randint(0, 19999)
                        mlp.train(np.array([[gameDataList[n].ball_position, gameDataList[n].wall_position]]), np.array([gameDataList[n].upOrDown[0], gameDataList[n].upOrDown[1]]))
                AI_type = 2
                print(" AI_type 2")
        if(AI_type == 2):
            if(mlp.predict(np.array([(ball.trt.ycor()+360), (b.trt.ycor()+4+360)]))[0][1] < mlp.predict(np.array([(ball.trt.ycor()+360), (b.trt.ycor()+4+360)]))[0][0]):
                b.up()
            else:
                b.down()








def set_keys(window, a, b):
    window.listen()
    window.onkeypress(a.up, "w")
    window.onkeypress(a.down, "s")
   # window.onkeypress(b.up, "Up")
   # window.onkeypress(b.down, "Down")

def windowSet(window):
    window.tracer(0)
    window.colormode(255)
    window.setup(width=1000, height=800)
    window.bgcolor("lightblue")

def main():
    window = turtle.Screen()
    windowSet(window)
    gra(window)


main()