#!/bin/bash
#
# release.sh  [DEPRECATED]
#
# The canonical release process is now the "Release" GitHub Actions workflow
# (.github/workflows/release.yml). It derives the next version automatically
# from conventional commits using git-cliff and runs the full verification
# suite before tagging.
#
# Trigger it via:
#   GitHub UI → Actions → Release → Run workflow
#
# This script is kept as a local emergency fallback only.
# ---------------------------------------------------------------------------
#
# Manual usage (emergency fallback):
#   ./release.sh <version>
#
# Example:
#   ./release.sh 3.1.0
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

NEW_VERSION="${1#v}"
TAG_NAME="v$NEW_VERSION"

echo "🚀 Starting release process for version $NEW_VERSION..."

# --- Check for unstaged changes ---
if ! git diff-index --quiet HEAD --; then
    echo "Error: You have unstaged changes. Please commit or stash them before running the release script."
    exit 1
fi

echo "✅ No unstaged changes found."

# --- Check branch and sync status ---
echo "🔍 Checking branch and sync status..."

# Fetch latest changes from remote
git fetch origin master

# Check if we're on master branch
CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)
if [ "$CURRENT_BRANCH" != "master" ]; then
    echo "Error: You are currently on branch '$CURRENT_BRANCH', not 'master'."
    echo "Please switch to master branch before releasing:"
    echo "  git checkout master"
    exit 1
fi

# Check if local master is behind origin/master
BEHIND_COUNT=$(git rev-list HEAD..origin/master --count)
if [ "$BEHIND_COUNT" -gt 0 ]; then
    echo "Error: Your local master branch is behind origin/master by $BEHIND_COUNT commit(s)."
    echo "Please pull the latest changes before releasing:"
    echo "  git pull origin master"
    exit 1
fi

echo "✅ On master branch and up-to-date with origin/master."

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

# --- Update CHANGELOG.md ---
echo "📝 Updating CHANGELOG.md..."
if command -v git-cliff &> /dev/null; then
    # Generate changelog for the new version
    git-cliff --output CHANGELOG.md
    
    # Replace [Unreleased] with the actual version number
    sed -i.bak "s/\[Unreleased\]/[$NEW_VERSION] - $(date +%Y-%m-%d)/" CHANGELOG.md
    rm CHANGELOG.md.bak
    
    echo "Updated CHANGELOG.md with changes for $TAG_NAME."
else
    echo "⚠️  Warning: git-cliff not found. Skipping CHANGELOG.md update."
    echo "   Install git-cliff: https://github.com/orhun/git-cliff"
fi

# --- Git Operations ---
echo "💾 Committing version changes..."
git add "$POM_FILE" "$PLUGIN_YML" "$README_FILE" "$JAVA_SOURCE_DIR" "CHANGELOG.md"
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
