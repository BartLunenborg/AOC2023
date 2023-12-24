# Python today, because z3 and in Java longs overflow
import sys
from z3 import Int, Solver

MIN = 200000000000000
MAX = 400000000000000


class Stone:
    def __init__(self, x, y, z, vx, vy, vz):
        self.x, self.y, self.z = x, y, z
        self.vx, self.vy, self.vz = vx, vy, vz


def readInput():
    file = open('../input/24.input').read().strip()
    stones = []
    for line in file.split('\n'):
        pos, vel = line.split('@')
        x, y, z = pos.split(', ')
        vx, vy, vz = vel.split(', ')
        stone = Stone(int(x), int(y), int(z), int(vx), int(vy), int(vz))
        stones.append(stone)
    return stones


def intersect(a, b):
    den = a.vy * -b.vx - a.vx * -b.vy
    if den == 0:
        return False
    numA = a.x * (a.y + a.vy) - a.y * (a.x + a.vx)
    numB = b.x * (b.y + b.vy) - b.y * (b.x + b.vx)
    px = (a.vx * numB - b.vx * numA) / den
    py = (a.vy * numB - b.vy * numA) / den
    if not ((px > a.x) == (a.vx > 0) and (px > b.x) == (b.vx > 0)):
        return False
    if MIN <= px <= MAX and MIN <= py <= MAX:
        return True


def partOne(stones):
    sum = 0
    for i in range(len(stones)):
        for j in range(i + 1, len(stones)):
            if (intersect(stones[i], stones[j])):
                sum += 1
    return sum


def partTwo(stones):
    x, y, z = Int('x'), Int('y'), Int('z')
    vx, vy, vz = Int('vx'), Int('vy'), Int('vz')
    f = [Int(f'f{i}') for i in range(len(stones))]
    s = Solver()
    for i in range(len(stones)):
        s.add(x + f[i]*vx - stones[i].x - f[i]*stones[i].vx == 0)
        s.add(y + f[i]*vy - stones[i].y - f[i]*stones[i].vy == 0)
        s.add(z + f[i]*vz - stones[i].z - f[i]*stones[i].vz == 0)
    s.check()
    return s.model().eval(x+y+z)


def main():
    stones = readInput()
    print(f"Part one: {partOne(stones)}")
    print(f"Part two: {partTwo(stones)}")


if __name__ == "__main__":
    main()
