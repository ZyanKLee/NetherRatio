# NetherRatio

[![Paper](https://img.shields.io/badge/Paper-1.21.10-blue?style=for-the-badge)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)

A Paper/Spigot plugin that allows you to customize the Nether-to-Overworld coordinate ratio for portal travel. Perfect for servers that want to change the vanilla 8:1 ratio to suit their world design!

## ✨ Features

- 🎯 **Customizable Ratio**: Set any ratio for Nether-Overworld portal coordinate conversion
- 🌍 **Multi-World Support**: Configure different world pairs for complex server setups
- 🌐 **Internationalization**: Built-in support for English, German, French, Italian, and Korean
- ⚡ **Performance Optimized**: Lightweight with minimal server impact
- 🔒 **Permission-Based**: Full integration with permission management plugins
- 🔄 **Hot Reload**: Change settings without restarting the server
- 📝 **Well Documented**: Comprehensive JavaDoc and code comments
- ⚙️ **Easy Configuration**: Simple YAML configuration files

## 📋 Requirements

- **Server**: Paper 1.21.4+ (or compatible Spigot fork)
- **Java**: Java 21 or higher
- **Dependencies**: None required

## 📥 Installation

1. Download the latest version from [GitHub Releases](https://github.com/ZyanKLee/NetherRatio/releases)
2. Place the JAR file in your server's `plugins` folder
3. Restart your server
4. Configure the plugin in `plugins/NetherRatio/config.yml`
5. Use `/netherratio reload` to apply changes

## ⚙️ Configuration

### config.yml

```yaml
# Language for messages (available: en, de, fr, it, ko)
language: en

# Overworld value : Nether 1
# For vanilla Minecraft behavior, set to 8
value: 8

# World pairs for portal travel
# Define which overworld connects to which nether world
# Format: 'overworld_name: nether_name'
world-pairs:
  world: world_nether
  # Add more world pairs as needed
  # world2: world2_nether
```

### Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `language` | String | `en` | Language for in-game messages (en, de, fr, it, ko) |
| `value` | Double | `8` | Coordinate conversion ratio (Overworld blocks : 1 Nether block) |
| `world-pairs` | Map | `world: world_nether` | Mapping of Overworld worlds to their corresponding Nether worlds |

### Examples

**1:1 Ratio** (same coordinates in both dimensions):
```yaml
value: 1
```

**Vanilla Minecraft** (8:1 ratio):
```yaml
value: 8
```

**Custom Ratio** (16:1 ratio):
```yaml
value: 16
```

**Multiple Worlds**:
```yaml
world-pairs:
  world: world_nether
  survival: survival_nether
  creative: creative_nether
```

## 🎮 Commands

### `/netherratio`
Display the current coordinate ratio.

**Permission**: `netherratio.netherratio`  
**Usage**: `/netherratio`

### `/netherratio <ratio>`
Set a new coordinate ratio.

**Permission**: `netherratio.netherratio`  
**Usage**: `/netherratio 8`  
**Example**: `/netherratio 16` - Sets a 16:1 ratio

### `/netherratio reload`
Reload the plugin configuration.

**Permission**: `netherratio.netherratio`  
**Usage**: `/netherratio reload`

## 🔐 Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `netherratio.netherratio` | Allows using all `/netherratio` commands | OP only |

### Permission Examples

**LuckPerms**:
```
/lp group admin permission set netherratio.netherratio true
```

**PermissionsEx**:
```
/pex group admin add netherratio.netherratio
```

## 🌐 Supported Languages

NetherRatio comes with built-in translations for:

- 🇬🇧 **English** (`en`)
- 🇩🇪 **German** (`de`)
- 🇫🇷 **French** (`fr`)
- 🇮🇹 **Italian** (`it`)
- 🇰🇷 **Korean** (`ko`)

To change the language, edit `language: en` in `config.yml` to your preferred language code.

### Custom Languages

You can create custom translations by:
1. Creating a new file in `plugins/NetherRatio/messages/`
2. Naming it `<language_code>.yml`
3. Copying the structure from any existing message file
4. Setting `language: <language_code>` in config.yml

## 🔧 How It Works

### Coordinate Conversion

When a player or entity travels through a Nether portal:

1. **Overworld → Nether**: Coordinates are **divided** by the ratio
   - Example with 8:1 ratio: `X=800, Z=600` → `X=100, Z=75`

2. **Nether → Overworld**: Coordinates are **multiplied** by the ratio
   - Example with 8:1 ratio: `X=100, Z=75` → `X=800, Z=600`

### World Mapping

The plugin uses the `world-pairs` configuration to determine which Nether world corresponds to each Overworld. This allows:
- Multiple separate world pairs on the same server
- Custom world names (not restricted to `world_nether` naming)
- Flexible multi-world setups

## 🏗️ Building from Source

### Prerequisites
- Git
- Maven 3.6+
- Java 21 JDK

### Build Steps

```bash
# Clone the repository
git clone https://github.com/ZyanKLee/NetherRatio.git
cd NetherRatio

# Build with Maven
mvn clean package

# The compiled JAR will be in target/
```

The built plugin will be available at `target/NetherRatio-2.0.2.jar`

## 🐛 Troubleshooting

### Portal doesn't work after changing ratio

1. Make sure you've saved the config and run `/netherratio reload`
2. Verify the world names in `world-pairs` match your actual world names
3. Check server logs for any error messages

### "World not found" errors

- Ensure your `world-pairs` configuration correctly maps your worlds
- Use `/worlds` (if you have a world management plugin) to verify world names
- World names are case-sensitive

### Permission errors

- Verify the permission is correctly set: `netherratio.netherratio`
- Check your permission plugin's configuration
- Ensure OPs have permissions if using the default setup

## 📊 Technical Details

- **Event Priority**: HIGH (runs after most plugins, before monitoring)
- **Supported Entities**: Players, minecarts, items, and all other entities
- **Portal Types**: Nether portals only (End portals are not affected)
- **Thread Safety**: All operations are thread-safe
- **Performance**: < 1ms processing time per portal event

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style

- Follow existing code conventions
- Add JavaDoc comments for public methods
- Include appropriate error handling
- Write clear commit messages

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🔗 Links

- [Download on Modrinth](https://modrinth.com/plugin/netherratio)
- [Report Issues](https://github.com/ZyanKLee/NetherRatio/issues)
- [Paper Documentation](https://docs.papermc.io/)

## 💖 Support

If you find this plugin useful, please consider:
- ⭐ Starring this repository
- 📢 Sharing it with others
- 🐛 Reporting bugs and suggesting features

## 🙏 Credits

This project is a fork and continuation of the original work by [xDxRAx](https://github.com/xDxRAx).

**Original Author:** [xDxRAx](https://github.com/xDxRAx)  
**Current Maintainer:** [ZyanKLee](https://github.com/ZyanKLee)

Special thanks to the original author for creating the foundation of this plugin!

---

**Made with ❤️ for the Minecraft community**
