# Random Equipment Attributes

一个 Minecraft NeoForge 模组，为你的装备随机附加额外属性。

---

## English

A Minecraft NeoForge mod that randomly grants bonus attributes to your equipment.

### Features

- **Random Attributes on Equip**: Every enchantable equipment piece (weapons, tools, armor) gets 1 to 4 random attributes the first time you equip it. Attributes are chosen from a per-item-type pool, with values following a half-normal distribution — most values cluster near zero, and both positive and negative outcomes are equally likely.
- **Reforging Station**: Place the equipment into the Reforging Station with a reforge material (configurable via the `random_equipment_attributes:reforge_materials` item tag), click reforge to roll a fresh set of random attributes. Each reforge incrementally boosts the chance of getting positive values — the more you reforge, the luckier your rolls become.
- **Configurable**: Adjust the attribute count range, base positive probability, reforge increment rate, and whether to show the reforge count in the item tooltip — all from the common config file.

---

## 中文

为你的装备随机附加额外属性。

### 功能

- **装备即随机**：每件可附魔的装备（武器、工具、护甲）第一次装备到身上时会自动获得 1 到 4 条随机属性。属性按物品类型从预设池中抽取，数值服从半正态分布，正负各占 50% 概率。
- **重铸台**：把装备和重铸材料（通过 `random_equipment_attributes:reforge_materials` 物品标签配置）一起放进重铸台，点击重铸即可重新随机一次属性。每次重铸会逐渐提高正面属性的出现概率，重铸次数越多，运气越好。
- **可配置**：属性数量范围、重铸起始正面概率、每次重铸的概率增量、是否在物品 tooltip 显示重铸次数，全部可以在 common 配置文件中自由调整。