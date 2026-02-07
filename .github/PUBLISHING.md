# Publishing Setup

Your GitHub Actions workflow for automated publishing has been created!

## Setup Instructions

### 1. Create Projects on Modrinth and Hangar

- **Modrinth**: https://modrinth.com/dashboard/projects
- **Hangar**: https://hangar.papermc.io/

### 2. Get API Tokens

**Modrinth:**
1. Go to https://modrinth.com/settings/pats
2. Create a new Personal Access Token
3. Give it write permissions for your project

**Hangar:**
1. Go to https://hangar.papermc.io/auth/settings/api-keys
2. Create a new API key
3. Give it permission to publish versions

### 3. Add Secrets to GitHub

1. Go to your repository: https://github.com/YOUR_USERNAME/NetherRatio/settings/secrets/actions
2. Add these secrets:
   - `MODRINTH_TOKEN`: Your Modrinth personal access token
   - `HANGAR_TOKEN`: Your Hangar API key
   - `MODRINTH_PROJECT_ID`: Your Modrinth project ID
   - `HANGAR_PROJECT_SLUG`: Your Hangar project slug (e.g., `username/NetherRatio`)

### 4. Add Project IDs to GitHub Secrets

In addition to the API tokens, add these secrets:
- `MODRINTH_PROJECT_ID`: Your Modrinth project ID (found in project settings)
- `HANGAR_PROJECT_SLUG`: Your Hangar project slug (e.g., `username/NetherRatio`)

### 5. Release Process

When you're ready to publish a new version:

```bash
# Update version and push tag
./release.sh 2.1.2
```

The workflow will automatically:
- Build the plugin with Maven
- Create a GitHub release with the JAR file
- Upload to Modrinth
- Upload to Hangar
- Include the changelog from CHANGELOG.md

No need to manually create a GitHub release - just push the tag!

### Supported Versions

The workflow is configured to mark the plugin as compatible with:
- Minecraft 1.21 through 1.21.11
- Paper, Spigot, and Purpur (Modrinth)
- Paper and Spigot (Hangar)

## Manual Publishing

You can also trigger the workflow manually by pushing a tag:

```bash
git tag v2.1.3
git push origin v2.1.3
```

## Important Files

- [.github/workflows/build-and-release.yml](.github/workflows/build-and-release.yml) - Unified workflow for GitHub, Modrinth, and Hangar
- [CHANGELOG.md](CHANGELOG.md) - Keep this updated for each release (used as release notes)
- [release.sh](release.sh) - Automated version bumping and tagging script
