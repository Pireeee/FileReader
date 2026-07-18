# 📂 **FileReader**

**FileReader** is a simple desktop application built with **Java 21 + JavaFX 21** to **scan and visualize disk space usage**.
It helps you find and navigate through the largest folders and files on your disk — quickly and clearly.

---

## 🚀 **Key Features**

✅ **Fast Disk Scan**

* Scan any disk or folder you choose.
* Runs asynchronously — keeps the UI responsive.

✅ **Detailed Tree View**

* Browse a full interactive folder/file tree.
* Shows sizes and percentages relative to parent folders.

✅ **Dynamic Treemap**

* Graphical visualization with an interactive treemap.
* Spot large folders/files instantly.

✅ **Live Statistics**

* Total scanned size.
* Number of files and folders scanned.
* Elapsed time and scan speed.

---

## 📦 **Download**

👉 Get the latest **V2.0.0 – Pre-release** from the [Releases](./releases) page.

---

## 🗂️ **How to Run**

1️⃣ Download the `.zip`.
2️⃣ Unzip it anywhere.
3️⃣ Double-click `FileReader.exe`.

✅ No need to install Java — a bundled runtime is included in the package.
✅ Tested on **Windows**.

> ⚠️ **Windows SmartScreen warning:** the executable isn't code-signed
> (a trusted certificate costs money — see [Release Status](#-release-status)
> below), so Windows will show a **"Windows protected your PC"** prompt.
> Click **More info → Run anyway** to launch it. This is expected and safe;
> it's just the cost of shipping an unsigned binary, not a sign of anything
> wrong with the app.

---

## ⚙️ **Built With**

* **Java 21**
* **JavaFX 21**
* **Clean Architecture** (domain, infrastructure, and UI layers are clearly separated)
* **ForkJoinPool** for asynchronous disk scanning
* Custom JavaFX components (`TreeTableView`, `Treemap`, etc.)

---

## 🧩 **Developer Commands**

For local development & debugging:

```bash
# Clean and rebuild
./gradlew clean build

# Run directly from source
./gradlew run

# Build the standalone Windows executable (FileReader.exe + bundled runtime)
./gradlew jpackageAppImage
# -> output in build/jpackage/FileReader/
```

`jpackageAppImage` requires a full JDK 21 (not just a JRE) since it shells
out to the `jpackage` tool bundled with the JDK. It produces a portable
**app-image** — no installer toolchain (WiX) needed — so the output folder
can be zipped and shared as-is; recipients just unzip and run `FileReader.exe`.

> The app's entry point for packaging is `fr.app.ui.Launcher`, a thin
> `main()` that delegates to `MainApp`. This indirection is required:
> jpackage runs the app off a plain classpath (not the module path), and
> JavaFX refuses to start when the class holding `main()` extends
> `Application` directly in that setup ("JavaFX runtime components are
> missing").

---

## 🔒 **Release Status**

> **Version:** `2.0.0` *(Pre-release)*
> ✅ Fully working standalone `FileReader.exe` (no Java install required)
> ⚠️ Ships as a portable app-image; a proper installer (`.msi`) would need WiX Toolset and may come later.
> ⚠️ Unsigned — triggers a Windows SmartScreen warning (see [How to Run](#️-how-to-run)).
> Code signing certificates are paid (or free via [SignPath.io](https://signpath.io)'s
> open-source program, subject to their review); not set up yet.

---

## ✨ **Authors**

* Pierre CLEMENT @Pireeee[https://github.com/Pireeee/]

---

## 📄 **License**

Distributed under the [MIT License](./LICENSE).

---

**Enjoy exploring your disk!** 🗃️✨

---
