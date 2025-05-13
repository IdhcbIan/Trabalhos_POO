/**
 * Helper method to add a villain type 2 with shooting parameters
 * @param linha Row position
 * @param coluna Column position
 * @param rate Shooting rate (higher = slower)
 * @param shootRight Direction to shoot (true = right, false = left)
 */
private void addVilao2(int linha, int coluna, int rate, boolean shootRight) {
    Villan_2 vilao = new Villan_2("Villan_1.png");
    vilao.setPosicao(linha, coluna);
    vilao.setFases(this); // Pass a reference to the Fases object
    vilao.setShootingRate(rate);
    vilao.setShootingDirection(shootRight);
    this.elementos.add(vilao);
}

/**
 * Configure level 3
 */
private void configureLevel3() {
    hero.setPosicao(2, 7);
    
    // Add fruits
    addFruta(4, 8);
    addFruta(11, 4);
    addFruta(18, 8);
    addFruta(25, 4);

    // Use regular villains instead of Villan_2
    addVilao(4, 1, true);
    addVilao(11, 1, true);
    addVilao(18, 1, true);
    addVilao(25, 1, true);
}

/**
 * Adds a fireball to the game elements
 * @param fireball The fireball to add
 */
public void addFireball(Fireball fireball) {
    this.elementos.add(fireball);
} 