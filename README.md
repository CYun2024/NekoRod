# NekoRod
A MC Paper plugin that allows you and another player to have a kitten together.

适用于**1.21.1 paper**，也可能适用于更高或更低的版本，但是我们没有测试。

Compatible with **Paper 1.21.1**. It may also work with higher or lower versions, but these have not been tested.

**说明：**
使用烈焰棒攻击另一名玩家，会给予该玩家 5秒的生命恢复2效果，有百分之30的概率生成少量经验，并同时增加一点Neko数值（以计分板实现），当这个值大于50时，每一次有百分之 （这个值-50）*2的概率生成一只名字为 {玩家B的名字}的猫崽 、主人为你的小猫，成功后将Neko数值清零

**Description:**
When you hit another player with a Blaze Rod:

The hit player receives Regeneration II for 5 seconds.

There's a 30% chance to drop a small amount of experience orbs.

It increases the target's Neko score (managed via scoreboard) by 1 point.

Once this Neko score exceeds 50, each subsequent hit has a (SCORE - 50) * 2% chance to spawn a kitten.

The kitten will be named {Player B's Name}'s kitten (We apologize that only Chinese is currently supported.).

You will become the kitten's owner.

If a kitten is successfully spawned, the Neko score is reset to 0.
