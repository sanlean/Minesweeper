# Minesweeper Core

Minesweeper Core is a Kotlin Multiplatform (KMP) library that provides the core logic for a Minesweeper game. This project serves as both a functional game engine and a robust template for creating Kotlin Multiplatform libraries. It supports a wide range of platforms, including Windows, macOS, Linux, Android, iOS, JavaScript, and JVM, making it an ideal starting point for developers looking to build cross-platform applications.

## Features

- **Minesweeper Game Logic**: Implements the core mechanics of the classic Minesweeper game, including board setup, mine placement, and game state management.
- **Kotlin Multiplatform Support**: Targets multiple platforms, including Windows, macOS, Linux (x64 and ARM64), Android, iOS, JavaScript, and JVM.
- **Ktor Client**: Configured with platform-specific clients for network operations.
- **SQLDelight**: Integrated for database operations with platform-specific drivers.
- **Modern Kotlin Libraries**: Utilizes the latest versions of Kotlin, Coroutines, Serialization, and DateTime libraries.
- **Maven Central Deployment**: Configured for easy deployment to Maven Central using GitHub Actions.

## Game Logic

The core logic of the Minesweeper game is encapsulated in the `Minesweeper` interface. It provides methods to interact with the game board, such as clicking cells and checking the game state. The `Builder` class allows for customizable game setup, including board dimensions and mine placement.

### Minesweeper Interface

```kotlin
interface Minesweeper {
    fun click(x: Int, y: Int): Boolean
    fun clickAsQuestioned(x: Int, y: Int): Boolean
    fun clickAsFlagged(x: Int, y: Int): Boolean
    fun isGameOver(): GameState
    val currentBoard: Board
    val save: Save
}
```

### Builder Class
The Builder class provides a fluent API for configuring the game:

- **Width and Height**: Set the dimensions of the game board.
- **Total Mines**: Specify the number of mines.
- **Random Mines**: Option to randomly place mines.
- **Save Adapter**: Customize save behavior with a SaveAdapter.

## Multiplatform Configuration
The project is configured to support a wide range of platforms, leveraging Kotlin Multiplatform capabilities. Each platform has specific configurations for Ktor clients and SQLDelight drivers.

### Supported Platforms
- **JVM**: Uses Apache5 client for Ktor and JVM driver for SQLDelight.
- **Android**: Configured with OkHttp client and Android driver for SQLDelight.
- **iOS**: Utilizes Darwin client and native driver for SQLDelight.
- **JavaScript**: Supports browser environments with Web Worker driver for SQLDelight.
- **Windows, macOS, Linux**: Configured with platform-specific clients and native drivers.


## Build and Deployment
The project is set up with a GitHub Actions pipeline for building and deploying to Maven Central. The build.gradle.kts file includes all necessary plugins and configurations for a seamless CI/CD process.

#### Key Plugins
- **Kotlin Multiplatform**: For cross-platform support.
- **SQLDelight**: For database operations.
- **Ktor**: For HTTP client operations.
- **Maven Publish**: For publishing to Maven Central.

## Getting Started
To get started with Minesweeper Core, clone the repository and open it in your favorite IDE. You can customize the game logic or use it as a template for your own Kotlin Multiplatform projects.

```shell
git clone https://github.com/sanlean/minesweeper.git
```

## License
Minesweeper Core is licensed under the MIT License. See the [LICENSE]() file for more details.

## Contributing
Contributions are welcome! Feel free to open issues or submit pull requests to improve the project.

## Contact
For questions or feedback, please contact [Leandro Santana](https://github.com/sanlean).