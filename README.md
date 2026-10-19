# Better With Bags

[![GitHub release](https://img.shields.io/github/v/release/MinecraftLabs/better-with-bags?color=brown)](https://github.com/MinecraftLabs/better-with-bags/releases)
[![GitHub Downloads](https://img.shields.io/github/downloads/MinecraftLabs/better-with-bags/total?color=brown)](https://github.com/MinecraftLabs/better-with-bags/releases)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/better-with-bags?color=brown&logo=modrinth)](https://modrinth.com/mod/better-with-bags)

Backpacks for Better Than Adventure.

Wear a backpack in the chestplate slot and your inventory gets extra rows. Hold one in your hand to open it, or put it down in the world like a chest. Each tier is crafted from the one before it, and the recipes and textures are made to fit the rest of BTA.

Works in singleplayer and on servers.

<p align="center">
  <img src="assets/backpacks_placed.png" width="80%" alt="All five backpacks placed in the world">
  <br>
  <em>All five backpacks, from the Small Leather Satchel to the Nether Explorer's Pack.</em>
</p>

## Backpacks

| Backpack | Slots | Bedroll slot | Movement |
|----------|-------|--------------|----------|
| Small Leather Satchel | 9 | no | normal |
| Medium Leather Backpack | 18 | no | normal |
| Large Leather Backpack | 27 | yes | up to 10% slower |
| Reinforced Traveler's Pack | 36 | yes | up to 15% slower |
| Nether Explorer's Pack | 27 | yes | normal, fireproof |

The slowdown depends on how full the backpack is. An empty backpack doesn't slow you down.

<p align="center">
  <img src="assets/gallery/satchel.png" width="49%" alt="Small Leather Satchel">
  <img src="assets/gallery/medium.png" width="49%" alt="Medium Leather Backpack">
  <img src="assets/gallery/large.png" width="49%" alt="Large Leather Backpack">
  <img src="assets/gallery/reinforced.png" width="49%" alt="Reinforced Traveler's Pack">
  <img src="assets/gallery/nether.png" width="49%" alt="Nether Explorer's Pack">
</p>

### Wearing

Put a backpack in the chestplate slot and its slots appear under your inventory, in creative too. The backpack is drawn on your back, with the bedroll if one is attached.

<p align="center">
  <img src="assets/backpack_inventory.png" width="80%" alt="Backpack slots under the player inventory">
  <br>
  <em>A worn Large Leather Backpack adds three rows under the inventory.</em>
</p>

<p align="center">
  <img src="assets/backpack_worn_front.png" width="25%" alt="Player wearing a backpack, front">
  <img src="assets/backpack_worn_back.png" width="25%" alt="Player wearing a backpack, back">
</p>

### Holding

Right-click with a backpack in your hand to open it.

### Placing

Sneak and right-click a block to place the backpack. Right-click it to open it, or sneak and right-click to pick it back up. Breaking it drops the backpack with everything still inside.

### Upgrading

Craft a backpack together with the materials of the next tier to upgrade it. The contents, bedroll and name carry over.

### Bedroll

A bed you can carry. You sleep in it like in a normal bed, but it doesn't set your spawn. Like a normal bed, it explodes in the Nether. Large, Reinforced and Nether backpacks have a slot to carry one.

<p align="center">
  <img src="assets/gallery/bedroll.png" width="80%" alt="Bedroll placed and attached to a backpack">
</p>

### Other details

- Rename a backpack with a label and the name shows in its screen, also when it is placed.
- The sort key sorts backpack contents.
- Dropped backpacks don't despawn.
- The Nether Explorer's Pack floats up out of lava.

## Requirements

- BTA == 8.0.1
- HalpLibe >= [6.2.0+8.0.1](https://github.com/Turnip-Labs/bta-halplibe/releases)
- Catalyst Core >= [3.0.5](https://github.com/MartinSVK12/catalyst/releases), also included in [Catalyst All](https://modrinth.com/mod/catalyst)

## Credits

- **badsbee** - GUI textures, model assets
- **Sour Zombo** - item textures

## License

See [LICENSE](LICENSE). Short version: free for personal use and free modpacks with attribution, no commercial use without permission.
