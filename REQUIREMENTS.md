# REQUIREMENTS.md - Phase 0

## Project Name
**Weather Dashboard APK**

## Description
Android app (APK) that displays current weather data for Neumarkt (Südtirol). Material Design UI. Data sourced from OpenDataHub API. Delivered as GitHub repo with CI/CD Actions.

## Goal
Provide a simple, native Android weather app using local open data (Südtirol).

## Users
- Ivan (personal use)
- Potential public release

## Requirements

### Must Have (P0)
- [ ] Fetch weather data from OpenDataHub API
- [ ] Display current weather for Neumarkt/Egna
- [ ] Material Design UI
- [ ] Build APK
- [ ] GitHub repo with GitHub Actions (CI/CD)

### Should Have (P1)
- [ ] Auto-refresh (e.g., every 30min)
- [ ] Error handling (offline, API down)
- [ ] Temperature + conditions + humidity

### Nice to Have (P2)
- [ ] Forecast (next days)
- [ ] Multiple locations
- [ ] Dark/Light theme

## Constraints
- **Budget:** €0 (free APIs, free GitHub)
- **Timeline:** TBD
- **Tech Stack:** Android (Kotlin/Java), Material Design, OpenDataHub API

## Success Criteria
- [ ] APK builds successfully via GitHub Actions
- [ ] Weather displays correctly for Neumarkt
- [ ] Material Design guidelines followed

## Risks
- OpenDataHub API rate limits
- API authentication required?
- Android build complexity

---

## ✅ User Confirmation

**Status:** ✅ Confirmed

**Confirmed by:** Ivan  
**Date:** 2026-03-06

**Notes:**
- Data source: https://databrowser.opendatahub.com/?search=Weather
- Deliverable: GitHub repo with Actions (auto-build APK)

---

## Next Phase
→ **Phase 1: ANALYSIS** (ResearchAgent)
