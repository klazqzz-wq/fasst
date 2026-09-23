package dev.fasst.eve;

/**
 * Jeden wpis na liście GUI.
 * name  - wyświetlana nazwa przedmiotu
 * model - id modelu przedmiotu (np. "minecraft:nether_star")
 * color - kolor nazwy w formacie RGB
 */
public record Preset(String name, String model, int color) {}
