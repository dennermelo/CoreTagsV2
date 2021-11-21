package com.github.dennermelo.core.tags.model.inventory;

import com.github.dennermelo.core.tags.model.inventory.format.InventoryFormat;
import com.github.dennermelo.core.tags.model.inventory.format.InventoryItem;
import com.github.dennermelo.core.tags.model.inventory.type.SlotItem;
import com.github.dennermelo.core.tags.util.ListUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * class to facilitate the construction of inventories.
 *
 * @param <T> the type of builder
 */
public class InventoryBuilder<T extends InventoryItem> {

    @Getter
    private final String inventoryName;
    @Getter
    private final Inventory inventory;
    private final HashMap<Object, Object> DATA = new HashMap<>();
    private final HashMap<ButtonType, Pair<Integer, ItemStack>> PAGES = new HashMap<>();
    private final ArrayList<InventoryFormat<T>> FORMATS = new ArrayList<>();
    @Getter
    private int page = 1;
    @Getter
    private int size = 0;
    private int exit;
    private int start = 0;
    private int value = 0;
    private Function<Integer, Boolean> scape;

    /**
     * @param name  the name of inventory
     * @param lines lines of inventory
     */
    public InventoryBuilder(String name, int lines) {
        int size = Math.min(6, Math.max(1, lines)) * 9;
        this.inventoryName = name.replace("&", "ยง");
        this.exit = size - 1;

        inventory = Bukkit.createInventory(new GuiHolder(event -> {
            int slot = event.getRawSlot();

            for (Map.Entry<ButtonType, Pair<Integer, ItemStack>> entry : PAGES.entrySet()) {
                if (entry.getValue().getKey() == slot) {
                    this.page = this.page + entry.getKey().value;
                    formatInventory();
                    return;
                }
            }

            for (InventoryFormat<T> format : FORMATS) {
                if (format.isValid(event.getRawSlot())) {
                    format.accept(event);
                    break;
                }
            }

        }), size, inventoryName.replace("{page}", String.valueOf(page)));
    }

    /**
     * build inventory
     *
     * @return the inventory
     */
    public Inventory build() {
        formatInventory();
        return this.inventory;
    }

    /**
     * set the page of inventory and size of objects in pages
     *
     * @param page the page
     * @param size the size
     * @return the builder
     */
    public InventoryBuilder<T> withPage(int page, int size) {
        this.page = page;
        this.size = size;

        return this;
    }

    /**
     * define the slot in which the items will start to be defined
     *
     * @param start the slot
     * @return the builder
     */
    public InventoryBuilder<T> withSlotStart(int start) {
        this.start = start;
        return this;
    }

    /**
     * skip slots and add to the current slot
     *
     * @param value the value to be added
     * @param scape the slots to skip
     * @return the builder
     */
    public InventoryBuilder<T> withSlotSkip(int value, int... scape) {
        this.value = value;
        this.scape = integer -> Arrays.stream(scape).anyMatch(slot -> slot == integer);

        return this;
    }

    /**
     * skip slots and add to the current slot
     *
     * @param value the value to be added
     * @param scape the function to skip
     * @return the builder
     */
    public InventoryBuilder<T> withSlotSkip(int value, Function<Integer, Boolean> scape) {
        this.value = value;
        this.scape = scape;

        return this;
    }

    /**
     * slot to stop setting items
     *
     * @param exit the exit
     * @return the builder
     */
    public InventoryBuilder<T> withSlotExit(int exit) {
        this.exit = exit;
        return this;
    }

    /**
     * set the item of inventory
     *
     * @param slot      The slot
     * @param itemStack The ItemStack
     * @param event     The {@link ClickEvent}
     * @return the builder
     */
    public InventoryBuilder<T> withItem(int slot, @Nullable ItemStack itemStack, @Nullable ClickEvent<T> event) {
        if (slot < 0) return this;

        FORMATS.add(new SingleInventoryFormat(slot, null, itemStack, event));
        return this;
    }

    /**
     * set the item of inventory with parameter T
     *
     * @param slot  The slot
     * @param value The parameter
     * @param event The {@link ClickEvent}
     * @return the builder
     */
    public InventoryBuilder<T> withItem(int slot, @NotNull T value, @Nullable ClickEvent<T> event) {
        if (slot < 0) return this;

        FORMATS.add(new SingleInventoryFormat(slot, value, value.getItem(getInventory(), this), event));
        return this;
    }

    /**
     * set the items of inventory
     *
     * @param items the items
     * @param event the {@link ClickEvent}
     * @return the builder
     */
    public InventoryBuilder<T> withItemsStacks(List<ItemStack> items, ClickEvent<T> event) {
        this.FORMATS.add(new MultiItemInventoryFormat(items, event));
        return this;
    }

    /**
     * set the items of inventory with parameter T
     *
     * @param items the items
     * @param event the the {@link ClickEvent}
     * @return the builder
     */
    public InventoryBuilder<T> withItems(List<T> items, ClickEvent<T> event) {
        this.FORMATS.add(new MultiValueInventoryFormat(items, event));
        return this;
    }

    /**
     * set the item to skip the inventory pages
     *
     * @param slot      the slot
     * @param itemStack the item
     * @return the builder
     */
    public InventoryBuilder<T> withNextPage(int slot, ItemStack itemStack) {
        PAGES.put(ButtonType.NEXT, Pair.of(slot, itemStack));
        return this;
    }

    /**
     * set the item to return the inventory pages
     *
     * @param slot      the slot of item
     * @param itemStack the item
     * @return the builder
     */
    public InventoryBuilder<T> withBackPage(int slot, ItemStack itemStack) {
        PAGES.put(ButtonType.BACK, Pair.of(slot, itemStack));
        return this;
    }

    /**
     * Add data to builder
     *
     * @param key   The key of data
     * @param value The value of data
     * @return The builder
     */
    public InventoryBuilder<T> addData(@NotNull Object key, @NotNull Object value) {
        DATA.put(key, value);
        return this;
    }

    /**
     * Get object of builder
     *
     * @param key The key of object
     * @return The object
     */
    @Nullable
    public Object getData(@NotNull Object key) {
        return DATA.get(key);
    }

    /**
     * Remove data from builder
     *
     * @param key The key of value
     */
    public void removeData(@NotNull Object key) {
        DATA.remove(key);
    }

    /**
     * Check if data exits in builder
     *
     * @param key The key
     * @return True if data exists
     */
    public boolean containsData(@NotNull Object key) {
        return DATA.containsKey(key);
    }

    /**
     * open inventory to player
     *
     * @param player the player
     * @return the builder
     */
    public InventoryBuilder<T> open(Player player) {

        if (this.inventory.getViewers().isEmpty()) {
            player.openInventory(this.inventory);
            formatInventory();
            player.updateInventory();

            return this;
        }

        player.openInventory(this.inventory);
        return this;
    }


    /**
     * Format the inventory and update items
     */

    private void formatInventory() {
        inventory.clear();

        FORMATS.forEach(format -> {
            if (format instanceof InventoryBuilder.MultiValueInventoryFormat) {
                MultiValueInventoryFormat value = (MultiValueInventoryFormat) format;
                value.map.clear();

                int slot = this.start;

                List<T> items = size <= 0 ? value.items : ListUtil.getSublist(value.items, page, size);
                for (int index = 0; index < items.size(); slot++) {
                    T item = items.get(index);
                    if (item instanceof SlotItem) {
                        SlotItem slotItem = (SlotItem) item;
                        if (slotItem.getSlot() >= 0) {
                            inventory.setItem(slotItem.getSlot(), slotItem.getItem(getInventory(), this));
                            value.map.put(slotItem.getSlot(), item);
                        }

                        index++;
                        continue;
                    }

                    if (slot > this.exit) {
                        break;
                    }

                    if (this.scape != null && this.scape.apply(slot)) {
                        slot += this.value - 1;
                        continue;
                    }

                    inventory.setItem(slot, item.getItem(getInventory(), this));
                    value.map.put(slot, item);

                    index++;
                }

                createPages(value.items.size());

            } else if (format instanceof InventoryBuilder.SingleInventoryFormat) {
                SingleInventoryFormat singleFormat = (SingleInventoryFormat) format;
                if (singleFormat.slot >= 0) {
                    inventory.setItem(singleFormat.slot, singleFormat.itemStack);
                }

            } else if (format instanceof InventoryBuilder.MultiItemInventoryFormat) {
                MultiItemInventoryFormat value = (MultiItemInventoryFormat) format;
                value.map.clear();

                int slot = this.start;

                List<ItemStack> items = size <= 0 ? value.items : ListUtil.getSublist(value.items, page, size);
                for (int index = 0; index < items.size(); slot++) {
                    if (slot > this.exit) {
                        break;
                    }

                    if (this.scape != null && this.scape.apply(slot)) {
                        slot += this.value - 1;
                        continue;
                    }

                    ItemStack item = items.get(index);
                    inventory.setItem(slot, item);
                    value.map.put(slot, item);

                    index++;
                }

                createPages(value.items.size());
            }
        });

    }

    /**
     * Create a pages of inventory
     *
     * @param size the size of list
     */
    private void createPages(int size) {
        if (this.page > 1 && PAGES.containsKey(ButtonType.BACK)) {
            Pair<Integer, ItemStack> pair = PAGES.get(ButtonType.BACK);
            inventory.setItem(pair.getKey(), pair.getValue());
        }

        if (this.size > 0 && PAGES.containsKey(ButtonType.NEXT) && size > this.page * this.size) {
            Pair<Integer, ItemStack> pair = PAGES.get(ButtonType.NEXT);
            inventory.setItem(pair.getKey(), pair.getValue());
        }
    }


    /**
     * Private enum of buttons to easy
     * skip and back pages of the inventory
     */
    @AllArgsConstructor
    private enum ButtonType {

        BACK(-1),
        NEXT(1);

        private final int value;
    }

    /**
     * Private class to format the inventory with more than one parameters T
     */
    private class MultiValueInventoryFormat implements InventoryFormat<T> {

        private final ArrayList<T> items;
        private final HashMap<Integer, T> map = new HashMap<>();

        private final ClickEvent<T> clickEvent;

        public MultiValueInventoryFormat(List<T> items, ClickEvent<T> clickEvent) {
            this.items = new ArrayList<>(items);
            this.clickEvent = clickEvent;
        }

        @Override
        public T getValue(int slot) {
            return map.get(slot);
        }

        @Override
        public boolean isValid(int value) {
            return map.containsKey(value);
        }

        public void accept(InventoryClickEvent event) {
            if (this.clickEvent != null) {
                clickEvent.onClick(event, InventoryBuilder.this, map.get(event.getRawSlot()));
            }
        }
    }

    /**
     * Private class to format the inventory with more than one item
     */
    private class MultiItemInventoryFormat implements InventoryFormat<T> {

        private final ArrayList<ItemStack> items;
        private final HashMap<Integer, ItemStack> map = new HashMap<>();
        private final ClickEvent<T> clickEvent;

        public MultiItemInventoryFormat(List<ItemStack> items, ClickEvent<T> clickEvent) {
            this.items = new ArrayList<>(items);
            this.clickEvent = clickEvent;
        }

        @Override
        public T getValue(int slot) {
            return null;
        }

        @Override
        public boolean isValid(int value) {
            return map.containsKey(value);
        }

        public void accept(InventoryClickEvent event) {
            if (clickEvent != null) {
                clickEvent.onClick(event, InventoryBuilder.this, null);
            }
        }
    }

    /**
     * Private class to format the inventory with just one item
     */
    @AllArgsConstructor
    private class SingleInventoryFormat implements InventoryFormat<T> {

        private final int slot;
        private final T value;
        private final ItemStack itemStack;
        private final ClickEvent<T> clickEvent;

        @Override
        public T getValue(int slot) {
            return value;
        }

        @Override
        public boolean isValid(int value) {
            return slot == value;
        }

        @Override
        public void accept(InventoryClickEvent event) {

            if (clickEvent != null) {
                clickEvent.onClick(event, InventoryBuilder.this, value);
            }

        }
    }
}