# Easy Inventory Manager (Bukera Brothers)

Kotlin + Jetpack Compose + Room. Basic feature set:

- Add / edit / delete products
- Quantity tracking
- Categories
- Search
- Stock in / stock out with history
- Total stock value / units dashboard
- Notes field per product

## Before first build

Gradle wrapper JAR/script is not included (no network access in this environment).
Run once locally, from the project root:

```
gradle wrapper --gradle-version 8.7
```

Then commit `gradlew`, `gradlew.bat`, and `gradle/wrapper/gradle-wrapper.jar`.

## CI

`.github/workflows/build.yml` builds a debug APK on push to `main`.

## Next phases (not included)

Intermediate: barcode/QR scanning, purchase/sales orders, transfers, CSV/Excel import-export, login + roles, audit log, dashboards.
Advanced: multi-warehouse, real-time sync, offline-first sync, batch/serial tracking, reorder automation, forecasting, POS/accounting integrations, label/invoice printing.
