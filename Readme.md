# Trabalho - POO: Bad Ice Cream

## Estrutura do Projeto

```tree
BadIceCream/
├── src/
│   ├── Main.java                 # Ponto de entrada da aplicação
│   ├── Modelo/                   # Entidades e modelos do jogo
│   │   ├── Personagem.java       # Classe base para todos os personagens
│   │   ├── Hero.java             # Personagem do jogador
│   │   ├── Fruta.java            # Itens colecionáveis
│   │   ├── Fogo.java             # Elemento de fogo
│   │   ├── Caveira.java          # Inimigo caveira
│   │   └── SuccessoNotification.java # Exibição de mensagem de sucesso
│   ├── Controler/                # Controladores de lógica do jogo
│   │   ├── Tela.java             # Tela principal e tratamento de entrada
│   │   └── ControleDeJogo.java   # Mecânicas do jogo e detecção de colisão
│   └── Auxiliar/                 # Classes auxiliares
│       ├── Consts.java           # Constantes do jogo
│       ├── Desenho.java          # Utilitários de desenho
│       └── Posicao.java          # Manipulação de posição
├── imgs/                         # Imagens do jogo
│   ├── Char_1.png                # Sprite do herói
│   ├── Fruit_1.png               # Sprite da fruta
│   ├── Ice_BG.png                # Tile de fundo
│   └── fire.png                  # Sprite do fogo
```

-> Organizacao.
- 3 Mapas 
- 3 Viloes

- Fase 1(Mapa 1, Vilao 1)
- Fase 2(Mapa 2, Vilao 1)
- Fase 1(Mapa 1, Vilao 2)
- Fase 1(Mapa 2, Vilao 2)
- Fase 1(Mapa 3, Vilao 3)
