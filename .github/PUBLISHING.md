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

### 4. Update Workflow File

Edit [.github/workflows/publish.yml](.github/workflows/publish.yml) and replace:
- `YOUR_MODRINTH_PROJECT_ID` with your actual Modrinth project ID (found in project settings)
- `YOUR_HANGAR_PROJECT_SLUG` with your Hangar project slug (e.g., `username/NetherRatio`)
- `YOUR_USERNAME` in the workflow file comments

### 5. Release Process

When you're ready to publish a new version:

```bash
# Update version and push tag
./release.sh 2.1.1

# Then create a GitHub release
# Go to: https://github.com/YOUR_USERNAME/NetherRatio/releases/new?tag=v2.1.1
# Click "Create release from tag"
```

The workflow will automatically:
- Build the plugin with Maven
- Upload to Modrinth
- Upload to Hangar
- Include the changelog from CHANGELOG.md

### Supported Versions

The workflow is configured to mark the plugin as compatible with:
- Minecraft 1.21 through 1.21.11
- Paper, Spigot, and Purpur (Modrinth)
- Paper and Spigot (Hangar)

## Manual Publishing

You can also trigger the workflow manually from GitHub Actions:
1. Go to Actions → Publish to Modrinth and Hangar
2. Click "Run workflow"
3. Enter the version number

## Important Files

- [.github/workflows/publish.yml](.github/workflows/publish.yml) - GitHub Actions workflow
- [CHANGELOG.md](CHANGELOG.md) - Keep this updated for each release
- [release.sh](release.sh) - Automated version bumping and tagging script
