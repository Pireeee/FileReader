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

👉 Get the latest **V1.0.0 – Pre-release** from the [Releases](./releases) page.

---

## 🗂️ **How to Run**

1️⃣ Download the `.zip`.
2️⃣ Unzip it anywhere.
3️⃣ Double-click `launch.bat`.

✅ No need to install Java — a custom runtime is included.
✅ Tested on **Windows**.

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
gradle clean build

# Run directly (if you have JavaFX SDK)
java --module-path "C:/javafx-sdk-21.0.7/lib" --add-modules javafx.controls,javafx.fxml -jar build/libs/FileReader-1.0.0.jar

# Or run with the custom runtime
runtime/bin/java --module fr.app/fr.app.ui.MainApp
```

---

## 🔒 **Release Status**

> **Version:** `1.0.0` *(Pre-release)*
> ✅ Fully working via `launch.bat`
> ⚠️ A standalone `.exe` installer will be added later.

---

## ✨ **Authors**

* Pierre CLEMENT @Pireeee[https://github.com/Pireeee/]

---

## 📄 **License**

Distributed under the [MIT License](./LICENSE).

---

**Enjoy exploring your disk!** 🗃️✨

---
