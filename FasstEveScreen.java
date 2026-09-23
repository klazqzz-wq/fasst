package dev.fasst.eve;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.List;

/**
 * GUI z presetami wyglądu. Zmiana dotyczy TYLKO Twojego klienta (podgląd lokalny) -
 * serwer o niczym nie wie, a przedmiot może wrócić do normalnego wyglądu
 * przy odświeżeniu ekwipunku przez serwer.
 */
public class FasstEveScreen extends Screen {

    // Edytuj tę listę wg uznania: nazwa, model (id itemu), kolor.
    private static final List<Preset> PRESETS = List.of(
            new Preset("Przykład: Gwiazda", "minecraft:nether_star", 0xFFD700),
            new Preset("Przykład: Smocze Jajo", "minecraft:dragon_egg", 0xAA00AA),
            new Preset("Przykład: Totem", "minecraft:totem_of_undying", 0x55FF55),
            new Preset("Przykład: Serce Morza", "minecraft:heart_of_the_sea", 0x55FFFF),
            new Preset("Przykład: Jabłko", "minecraft:enchanted_golden_apple", 0xFFAA00)
    );

    private static final int BTN_W = 200;
    private static final int BTN_H = 20;

    private Text status = Text.empty();

    public FasstEveScreen() {
        super(Text.literal("FasstEve - podgląd lokalny"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - BTN_W / 2;
        int y = 40;

        for (Preset p : PRESETS) {
            addDrawableChild(ButtonWidget.builder(Text.literal(p.name()), b -> apply(p))
                    .dimensions(x, y, BTN_W, BTN_H).build());
            y += BTN_H + 4;
        }

        y += 8;
        addDrawableChild(ButtonWidget.builder(Text.literal("Przywróć oryginalny wygląd"), b -> reset())
                .dimensions(x, y, BTN_W, BTN_H).build());
        y += BTN_H + 4;
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> close())
                .dimensions(x, y, BTN_W, BTN_H).build());
    }

    private ItemStack heldStack() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return ItemStack.EMPTY;
        return mc.player.getMainHandStack();
    }

    private void apply(Preset p) {
        ItemStack stack = heldStack();
        if (stack.isEmpty()) {
            status = Text.literal("Weź przedmiot do ręki.").formatted(Formatting.RED);
            return;
        }
        Identifier model = Identifier.tryParse(p.model());
        if (model == null) {
            status = Text.literal("Nieprawidłowe id modelu.").formatted(Formatting.RED);
            return;
        }
        stack.set(DataComponentTypes.CUSTOM_NAME,
                Text.literal(p.name()).setStyle(Style.EMPTY.withColor(p.color()).withItalic(false)));
        stack.set(DataComponentTypes.ITEM_MODEL, model);
        stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(
                Text.literal("[Podgląd lokalny - widoczny tylko dla Ciebie]")
                        .formatted(Formatting.GRAY, Formatting.ITALIC))));
        status = Text.literal("Zmieniono wygląd (lokalnie).").formatted(Formatting.GREEN);
    }

    private void reset() {
        ItemStack stack = heldStack();
        if (stack.isEmpty()) {
            status = Text.literal("Weź przedmiot do ręki.").formatted(Formatting.RED);
            return;
        }
        stack.remove(DataComponentTypes.CUSTOM_NAME);
        stack.remove(DataComponentTypes.ITEM_MODEL);
        stack.remove(DataComponentTypes.LORE);
        status = Text.literal("Przywrócono (jeśli przedmiot miał własną nazwę od serwera, odśwież ekwipunek).")
                .formatted(Formatting.YELLOW);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_ALT) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Przedmiot w ręce - zmiana tylko na Twoim ekranie").formatted(Formatting.GRAY),
                this.width / 2, 25, 0xAAAAAA);
        context.drawCenteredTextWithShadow(this.textRenderer, status, this.width / 2, this.height - 20, 0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
