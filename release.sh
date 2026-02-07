#!/bin/bash
#
# release.sh
#
# A script to automate the release process for the NetherRatio plugin.
#
# This script performs the following actions:
# 1. Validates that a version number is provided.
# 2. Updates the version in pom.xml, plugin.yml, and all .java source files.
# 3. Commits the changes with a standardized message.
# 4. Creates a Git tag for the new version.
# 5. Pushes the commit and the tag to the remote repository.
#
# Usage:
#   ./release.sh <version>
#
# Example:
#   ./release.sh 2.1.0
#

# --- Configuration ---
set -e # Exit immediately if a command exits with a non-zero status.

POM_FILE="pom.xml"
PLUGIN_YML="src/main/resources/plugin.yml"
README_FILE="README.md"
JAVA_SOURCE_DIR="src/main/java"

# --- Validation ---
if [ -z "$1" ]; then
  echo "Error: No version number provided."
  echo "Usage: ./release.sh <version>"
  exit 1
fi

NEW_VERSION=$1
TAG_NAME="v$NEW_VERSION"

echo "🚀 Starting release process for version $NEW_VERSION..."

# --- Check for unstaged changes ---
if ! git diff-index --quiet HEAD --; then
    echo "Error: You have unstaged changes. Please commit or stash them before running the release script."
    exit 1
fi

echo "✅ No unstaged changes found."

# --- Update version in pom.xml ---
echo "🔧 Updating version in $POM_FILE..."
# Use awk to only replace the project version (first <version> after <artifactId>)
awk -v new_version="$NEW_VERSION" '
    /<artifactId>NetherRatio<\/artifactId>/ { found=1 }
    found && /<version>/ && !replaced { 
        sub(/<version>.*<\/version>/, "<version>" new_version "</version>")
        replaced=1
    }
    { print }
' "$POM_FILE" > "$POM_FILE.tmp" && mv "$POM_FILE.tmp" "$POM_FILE"
echo "Updated $POM_FILE to version $NEW_VERSION."

# --- Update version in plugin.yml ---
echo "🔧 Updating version in $PLUGIN_YML..."
# Only update the plugin version line (line 2), not api-version
sed -i.bak "2s/version: '.*'/version: '$NEW_VERSION'/" "$PLUGIN_YML"
rm "${PLUGIN_YML}.bak"
echo "Updated $PLUGIN_YML to version $NEW_VERSION."

# --- Update version in README.md ---
echo "🔧 Updating version in $README_FILE..."
# This updates the JAR filename in the build section
sed -i.bak "s/NetherRatio-.*\.jar/NetherRatio-$NEW_VERSION.jar/" "$README_FILE"
rm "${README_FILE}.bak"
echo "Updated JAR version in $README_FILE."

# --- Update @version in all .java files ---
echo "🔧 Updating @version tag in all Java files..."
find "$JAVA_SOURCE_DIR" -type f -name "*.java" -exec sed -i.bak "s/@version .*/@version $NEW_VERSION/" {} +
find "$JAVA_SOURCE_DIR" -type f -name "*.java.bak" -delete
echo "Updated @version tags in all .java files."

# --- Git Operations ---
echo "💾 Committing version changes..."
git add "$POM_FILE" "$PLUGIN_YML" "$README_FILE" "$JAVA_SOURCE_DIR"
git commit -m "chore: Release $TAG_NAME"

echo "🏷️  Tagging new version..."
git tag "$TAG_NAME"

echo "📤 Pushing commit and tag to remote..."
git push
git push origin "$TAG_NAME"

echo "🎉 Release process complete!"
echo ""
echo "The tag v$NEW_VERSION has been pushed to GitHub."
echo "GitHub Actions will now automatically:"
echo "  1. Build the plugin"
echo "  2. Create a GitHub release"
echo "  3. Publish to Modrinth"
echo "  4. Publish to Hangar"
echo ""
echo "Check workflow status: https://github.com/YOUR_USERNAME/NetherRatio/actions"
