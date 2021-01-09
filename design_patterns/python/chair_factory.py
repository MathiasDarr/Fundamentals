from abc import ABCMeta, abstractstaticmethod


class ChairInterface(metaclass=ABCMeta):
    @staticmethod
    def get_dimensions():
        """ Chair Interface """


class BigChair(ChairInterface):

    def __init__(self):
        self.height = 80
        self.width = 80
        self.depth = 80

    def get_dimensions(self):
        return {"height": self.height, "width": self.width, "depth": self.depth}


class SmallChair(ChairInterface):

    def __init__(self):
        self.height = 40
        self.width = 40
        self.depth = 40

    def get_dimensions(self):
        return {"height": self.height, "width": self.width, "depth": self.depth}


class ChairFactory:
    @staticmethod
    def get_chair(chairtype):
        try:
            if chairtype == "BigChair":
                return BigChair()
            if chairtype == "SmallChair":
                return SmallChair()

            raise AssertionError("Chair not found")
        except AssertionError as _e:
            print(_e)


if __name__ == "__main__":
    BCHAIR = ChairFactory.get_chair("BigChair")
    print(BCHAIR.get_dimensions())

    SCHAIR = ChairFactory.get_chair("SmallChair")
    print(SCHAIR.get_dimensions())
