<img width="1280" height="1280" alt="AutoTrade" src="https://github.com/user-attachments/assets/faf0b6d0-3427-49ca-80b3-26af8b867b56" />

This mod allows you to trade with villagers automatically without keeping the GUI open. Press **V** to toggle autotrading (instant trade → proximity trade → off).

## Sell Setup

* Do `/autotrade add [item]` to add an item to the pool of items you want to sell to villagers. Use a valid Minecraft item such as `iron_ingot` or `melon`. The mod will let you know if the item exists in the Minecraft registry.
* Do `/autotrade remove [item]` to remove an item from the sell pool.
* Do `/autotrade list` to list all the items in the sell pool.

## Buy Setup

* Do `/autotrade addbuy [item]` to add an item you want to buy from villagers (the item you receive).
* Do `/autotrade removebuy [item]` to remove an item from the buy pool.
* Do `/autotrade listbuy` to list all the items in the buy pool.

## Trade Mode

* Do `/autotrade mode sell` to only sell items from your sell list to villagers (you give the item, receive emeralds).
* Do `/autotrade mode buy` to only buy items from your buy list from villagers (you pay emeralds, receive the item).
* Do `/autotrade mode both` to do both in the same session, using each list for its respective trade direction.

The selected mode is saved to your config and persists across game restarts.

## Note

In order to get a clean click that doesn't open the GUI, autotrade tricks the server by sending a packet that doesn't update the user inventory. This means that your items will not visually update until the player opens a container such as a chest, shulker box, furnace, or another villager. This is 100% safe and is intended behavior.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
