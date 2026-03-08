# Vercel – Recherche-Report

**Erstellt:** Februar 2025  
**Sprache:** Deutsch  
**Scope:** Unternehmensprofil, Technologie-Stack, GitHub-Präsenz, Konkurrenz & Bewertung

---

## 1. Unternehmensprofil & Geschichte

### Was ist Vercel?

Vercel ist eine amerikanische Cloud-Plattform für Frontend-Entwickler und -Teams. Das Unternehmen bietet Tools, Frameworks und Cloud-Infrastruktur zum Erstellen und Betreiben von Webanwendungen mit Fokus auf **Developer Experience (DX)** und **Jamstack-Architektur**.

### Gründer & Geschichte

| **Attribut** | **Details** |
|--------------|-------------|
| **Gründung** | 2015 (als ZEIT) |
| **Umbenennung** | April 2020 (ZEIT → Vercel) |
| **CEO/Gründer** | Guillermo Rauch |
| **Hauptsitz** | San Francisco, Kalifornien, USA |
| **Mitarbeiter** | ~550 (Stand 2025) |
| **Unternehmenswert** | $3,25 Milliarden (Serie E, Mai 2024) |

### Guillermo Rauch – Der Gründer

- Geboren Dezember 1990 in Lanús, Argentinien
- Autodidaktischer Entwickler, High-School-Dropout
- Zog mit 18 Jahren nach San Francisco
- Gründete sein erstes Unternehmen mit 11 Jahren
- Bekannt als Schöpfer von **Socket.IO** (2010) und **Next.js** (2016)
- Führte Vercel 2025 auf eine Bewertung von **$9 Milliarden** (Verdreifachung innerhalb eines Jahres)

### Finanzierungsrunden

| **Runde** | **Jahr** | **Betrag** | **Bewertung** |
|-----------|----------|------------|---------------|
| Serie A | 2020 | $21 Mio | - |
| Serie C | 2021 | $102 Mio | - |
| Serie E | 2024 | $250 Mio | $3,25 Mrd |

### Übernahmen durch Vercel

| **Unternehmen** | **Datum** | **Bedeutung** |
|-----------------|-----------|---------------|
| Turborepo | Dezember 2021 | Monorepo-Build-System |
| Splitbee | Oktober 2022 | Analytics-Tool |
| Tremor | Januar 2025 | React-Komponenten-Bibliothek |
| NuxtLabs | Juli 2025 | Vue/Nuxt.js-Ökosystem |

---

## 2. Produktübersicht & Features

### Hauptprodukt: Vercel Platform

Die Vercel-Plattform ist ein **Frontend-Cloud-Hosting-Service**, der auf drei Säulen basiert:

1. **Develop** – Enwicklung mit modernen Frameworks
2. **Preview** – Automatische Preview-Deployments
3. **Ship** – Produktions-Deployments mit globalem CDN

### Kernfeatures

| **Feature** | **Beschreibung** |
|-------------|------------------|
| **Git-basierte Deployments** | Automatisches Deployment bei Git-Push (GitHub, GitLab, Bitbucket) |
| **Preview Deployments** | Jeder Pull Request erhält eine eigene Live-Preview-URL |
| **Globales Edge-Netzwerk** | AWS-basiertes CDN mit weltweiten Points of Presence |
| **Serverless Functions** | API-Routen und Edge-Functions ohne Server-Management |
| **Fluid Compute** | Neue Architektur (2025): Concurrent Request-Verarbeitung mit Serverless-Elasticität |
| **Web Application Firewall** | Integrierter DDoS-Schutz, Bot-Management, Rate-Limiting |
| **Image Optimization** | Automatische Bildoptimierung bei Auslieferung |

### Framework-Unterstützung

Vercel unterstützt offiziell:
- **Next.js** (erste Klasse – eigenes Framework)
- SvelteKit, Nuxt.js, Gatsby, Remix
- Astro, Vue, Angular, React
- Statische HTML/JS Sites

---

## 3. Technologie-Stack

### Next.js – Das Flaggschiff-Framework

| **Metrik** | **Wert** |
|------------|----------|
| **GitHub Stars** | ~137.000 |
| **Forks** | ~30.000 |
| **Lizenz** | MIT (Open Source) |
| **Aktivität** | Sehr hoch (tägliche Releases) |

**Next.js Features:**
- Server-Side Rendering (SSR)
- Static Site Generation (SSG)
- Incremental Static Regeneration (ISR)
- App Router (React Server Components)
- API Routes
- Edge Runtime
- Turbopack (Rust-basierter Bundler)

### Vercel Edge Network / Edge Functions

- **Runtime:** V8-Isolates (ähnlich Cloudflare Workers)
- **Cold Start:** Nahezu null Millisekunden
- **Geolocation:** Automatische Region-Auswahl
- **Middleware:** Edge-basierte Request-Verarbeitung

### Turborepo

- **Beschreibung:** High-Performance-Build-System für Monorepos
- **Features:**
  - Content-aware Hashing
  - Inkrementelle Builds
  - Remote Caching (kostenlos bei Vercel)
  - Parallelle Task-Ausführung
- **Akquiriert:** Dezember 2021 durch Vercel

### SWR (Stale-While-Revalidate)

- **Beschreibung:** React-Hooks für Datenabfrage
- **Verwendung:** Client-seitiges Caching und Revalidierung
- **Stars:** ~30.000+ auf GitHub
- **Vorteil:** Sofortige UI-Aktualisierungen mit Hintergrund-Sync

### Weitere Vercel-Technologien

| **Technologie** | **Zweck** |
|-----------------|-----------|
| **Turbopack** | Rust-basierter Bundler (Nachfolger von Webpack) |
| **v0** | AI-gestützte UI-Generierung |
| **Vercel AI SDK** | Toolkit für KI-Anwendungen |
| **Vercel CLI** | Kommandozeilenwerkzeug für Deployments |

---

## 4. GitHub-Präsenz

### Haupt-Repositories

#### vercel/next.js

| **Metrik** | **Stand Februar 2025** |
|------------|------------------------|
| **Stars** | ~137.000 |
| **Forks** | ~30.000 |
| **Issues** | Aktiv verwaltet |
| **Contributors** | 3.000+ (kumulativ) |
| **Letzter Commit** | Täglich (mehrere PRs) |
| **Release-Zyklus** | Wöchentlich/Alle zwei Wochen |

**Aktive Maintainer:**
- Sebastian Silbermann (@eps1lon)
- Jiwon Choi (@devjiwonchoi)
- Tobias Koppers (@sokra) – Webpack/Turbopack
- JJ Kasper (@ijjk)
- Tim Neutkens (@timneutkens)

#### vercel/vercel (CLI)

| **Metrik** | **Stand** |
|------------|-----------|
| **Name** | Vercel CLI |
| **Funktion** | Deployment, Konfiguration, Management |
| **Struktur** | Monorepo (pnpm) |
| **Installation** | `npm i -g vercel` |
| **Aktivität** | Hoch |

### Weitere wichtige Repositories

| **Repository** | **Beschreibung** | **Stars (geschätzt)** |
|----------------|------------------|----------------------|
| vercel/turbo | Turborepo Build-System | ~25.000+ |
| vercel/swr | React-Daten-Hooks | ~30.000+ |
| vercel/ai | AI SDK | ~10.000+ |
| vercel/turbopack | Rust-Bundler | ~5.000+ |
| vercel/platforms | Multi-Tenant-Plattform-Template | ~5.000+ |
| vercel/storage | Storage-Integrationen | ~2.000+ |

### Open Source Commitment

- Next.js ist vollständig Open Source (MIT)
- Bug Bounty Programm für Sicherheitslücken
- Aktive Community auf Discord und GitHub Discussions
- Regelmäßige Maintainer-Updates und Roadmaps

---

## 5. Enterprise & Open Source

### Vercel AI SDK

**Beschreibung:** TypeScript-Toolkit für KI-gestützte Anwendungen

| **Komponente** | **Zweck** |
|----------------|-----------|
| **AI SDK Core** | Einheitliche API für LLM-Integration (Textgenerierung, Tool-Calls) |
| **AI SDK UI** | Framework-agnostische Hooks für Chat/GUI |
| **AI SDK RSC** | React Server Components für Streaming |

**Unterstützte Provider:**
- OpenAI (GPT-4, GPT-3.5)
- Anthropic (Claude)
- Google (Gemini)
- Mistral, Groq, Azure, AWS Bedrock
- Selbstgehostete Modelle

### v0 – AI UI Generation

| **Attribut** | **Details** |
|--------------|-------------|
| **Launch** | 2023 |
| **Funktion** | UI-Komponenten aus natürlichsprachlichen Prompts |
| **Auszeichnung** | Webby Award 2025 (Developer Tools) |
| **Integration** | Direkte Publikation zu Vercel |
| **Sync** | GitHub-Repository-Integration |

**v0 Features:**
- Design Mode für visuelle Feinanpassung
- Template-Bibliothek
- Design-System-Erstellung
- Agentic Workflow (plant, erstellt Tasks, verbindet Datenbanken)
- iOS-App für mobile Entwicklung

### Ecosystem-Partnerschaften

- **Cloudflare:** CDN-Integrationen
- **AWS:** Infrastruktur-Backend
- **MongoDB, Neon, Supabase:** Datenbank-Integrationen
- **Auth0, Clerk:** Authentifizierungslösungen
- **Sanity, Contentful:** CMS-Integrationen

---

## 6. Preisgestaltung & Pläne

### Übersicht: Drei Tarife

| **Plan** | **Preis** | **Zielgruppe** |
|----------|-----------|----------------|
| **Hobby** | Kostenlos | Persönliche Projekte, Entwickler |
| **Pro** | $20/Nutzer/Monat | Professionelle Teams, Freelancer |
| **Enterprise** | Individuell | Große Unternehmen, Organisationen |

### Hobby Plan (Free Tier)

**Inkludiert:**
- Unbegrenzte Deployments
- 1 Million Edge Requests/Monat
- 100 GB Fast Data Transfer/Monat
- 1 Stunde Runtime Logs
- HTTPS/SSL automatisch
- Preview Deployments für jeden Git-Push
- Web Application Firewall (Basis)
- DDoS Protection

**Einschränkungen:**
 Keine kommerzielle Nutzung
- Keine Team-Kollaboration
- Keine zusätzliche Nutzung kaufbar
- Keine SLA

### Pro Plan

**Base-Preis:** $20/Nutzer/Monat + $20 Credits inkludiert

**Zusätzliche Features:**
- Team-Kollaboration (kostenlose Viewer-Seats)
- Credit-basiertes Billing
- Advanced Analytics
- 10 Millionen Edge Requests/Monat
- 1 TB Fast Data Transfer/Monat
- Keine Build-Warteschlangen
- Cold Start Prevention
- E-Mail-Support
- Enterprise Add-ons verfügbar

**On-Demand-Preise (Pay-as-you-go):**
| **Ressource** | **Preis** |
|---------------|-----------|
| Edge Requests | ~$0.60 pro 1 Million |
| Fast Data Transfer | ~$0.15 pro GB |
| Serverless Functions | ~$0.40 pro GB-Hour |
| Build Time | ~$0.10 pro Minute |

### Enterprise Plan

**Mindestverpflichtung:** ~$20.000–25.000/Jahr

**Enthält alles aus Pro, plus:**
- Single Sign-On (SSO)
- SCIM & Directory Sync
- 99.99% Uptime SLA
- Multi-Region Compute & Failover
- Managed WAF Rulesets
- Advanced Observability
- Dedicated Support
- Custom Contracts

### Hidden Costs (Achtung!)

- **Enterprise-Sprung:** Keine Mittelstufe zwischen Pro (~$60 für 3 Entwickler) und Enterprise (~$20k+)
- **Bandbreite:** Bei hohem Traffic teuer
- **Build-Minuten:** Große Monorepos können schnell kostenintensiv werden
- **Edge-Requests:** Bei API-intensiven Apps relevant

---

## 7. Konkurrenz & Alternativen

### Direkte Konkurrenten

#### Netlify

| **Kriterium** | **Netlify** |
|---------------|-------------|
| **Stärken** | Großzügiger Free Tier, gute Jamstack-Integration, tolle DX |
| **Preise** | Free: 300 Credits/Monat, Pro: 1.000 Credits/Monat |
| **Besonderheiten** | Netlify Drop, Form Handling, Identity |
| **Für wen?** | Jamstack-Entwickler, kleine Teams |
| **Nachteile** | Weniger optimiert für Next.js/SSR als Vercel |

#### Cloudflare Pages

| **Kriterium** | **Cloudflare Pages** |
|---------------|----------------------|
| **Stärken** | Extrem globales Netzwerk, vorhersehbare Preise, schnelle Caching |
| **Preise** | Free: Unbegrenzte Builds, Pro: $20/Monat |
| **Besonderheiten** | Integration mit Workers, KV-Storage, D1-Datenbank |
| **Für wen?** | Performance-kritische Apps, globale Auslieferung |
| **Nachteile** | Node-APIs erfordern Worker-Runtime-Anpassungen, weniger Next.js-Integration |

#### AWS Amplify

| **Kriterium** | **AWS Amplify** |
|---------------|-----------------|
| **Stärken** | Tiefere AWS-Integration, Full-Stack-Features |
| **Preise** | Pay-as-you-go, Free Tier: 1.000 Build-Minuten/Monat |
| **Besonderheiten** | Cognito Auth, AppSync GraphQL, S3-Storage |
| **Für wen?** | Teams, die bereits auf AWS sind |
| **Nachteile** | Komplexer, höherer Operations-Aufwand |

#### Railway

| **Kriterium** | **Railway** |
|---------------|-------------|
| **Stärken** | Einfache Deployment-Erfahrung, gute Datenbank-Integration |
| **Preise** | Starter: $5/Monat, Pro: $20/Nutzer |
| **Besonderheiten** | Automatische Datenbank-Provisionierung |
| **Für wen?** | Full-Stack-Entwickler, die schnell skalieren wollen |
| **Nachteile** | Weniger reif als Vercel/Netlify |

#### Render

| **Kriterium** | **Render** |
|---------------|------------|
| **Stärken** | Einfachheit, gute Preis-Leistung |
| **Preise** | Free: Web-Services, Standard: $7/Monat |
| **Besonderheiten** | Native Docker-Support, private Netzwerke |
| **Für wen?** | Entwickler, die traditionellere Hosting-Modelle bevorzugen |
| **Nachteile** | Weniger optimiert für Frontend-Frameworks |

### Vergleichstabellen

#### Feature-Vergleich

| **Feature** | **Vercel** | **Netlify** | **Cloudflare** | **AWS Amplify** |
|-------------|------------|-------------|----------------|-----------------|
| **Next.js-Optimierung** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐ | ⭐⭐ |
| **Edge-Functions** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **Free Tier** | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |
| **Dokumentation** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| **Preis-Transparenz** | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ |
| **Enterprise-Features** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

#### Preisvergleich (Typisches Projekt: 3 Entwickler, mittlerer Traffic)

| **Plattform** | **Geschätzte Monatskosten** |
|----------------|---------------------------|
| Vercel Pro | $60–150/Monat |
| Netlify Pro | $100–200/Monat |
| Cloudflare Pro | $20–50/Monat |
| AWS Amplify | $50–300/Monat (hohe Varianz) |
| Railway | $60–120/Monat |
| Render | $20–80/Monat |

---

## 8. Developer Experience (DX)

### Deployment-Workflow

1. **Git-Integration** → Projekt importieren
2. **Build-Konfiguration** → Automatisch erkannt (zero-config)
3. **Preview-Deployment** → Jeder PR erhält URL
4. **Production-Deployment** → Merge auf main → Live

**Besonderheiten:**
- **Zero Config:** Vercel erkennt Framework automatisch
- **Git-Centric:** Alles läuft über Git-Workflows
- **Atomic Deploys:** Rollback auf vorherige Version jederzeit möglich

### Preview Deployments

- **Funktion:** Jeder Branch erhält eigene Live-URL
- **Vorteil:** Stakeholder können Features vor dem Merge reviewn
- **Integration:** Kommentare direkt im Pull Request

### Analytics & Monitoring

| **Feature** | **Beschreibung** |
|-------------|------------------|
| **Real Experience Score** | Core Web Vitals-Tracking |
| **Traffic Insights** | Besucherstatistiken |
| **Performance** | Ladezeiten, API-Latenz |
| **Runtime Logs** | Serverless Function Logs |
| **Audit Logs** | Enterprise: Alle Aktivitäten |

### Vercel CLI

```bash
# Installation
npm i -g vercel

# Login
vercel login

# Deployment
vercel

# Production-Deployment
vercel --prod

# Projekt-Logs
vercel logs [project]
```

**CLI-Features:**
- Lokale Entwicklung mit `vercel dev`
- Umgebungsvariablen-Management
- Secrets-Management
- Branch-basierte Deployments

---

## 9. Bewertung: Für wen ist Vercel geeignet?

### Empfohlen für:

✅ **Next.js-Entwickler** – Best-in-Class-Integration
✅ **React-Teams** – Optimierte Frontend-Infrastruktur
✅ **Schnell wachsende Startups** – Skalierbar ohne Ops-Aufwand
✅ **Design-fokussierte Teams** – Preview-Workflows, v0-Integration
✅ **AI-gestützte Projekte** – AI SDK und v0
✅ **Globale Anwendungen** – Edge-Netzwerk weltweit

### Nicht empfohlen für:

❌ **Kostensensible Projekte mit hohem Traffic** – Bandbreite kann teuer werden
❌ **Teams, die Enterprise-Features benötigen, aber kein $20k+/Jahr Budget haben** – Lücke zwischen Pro und Enterprise
❌ **Nicht-Frontend-Workloads** – Backend-heavy Apps können besser woanders laufen
❌ **Vendor-Lock-in-äverse Teams** – Enge Integration mit Next.js/Vercel-Ökosystem

### Entscheidungshilfe

| **Szenario** | **Empfehlung** |
|--------------|----------------|
| Next.js-App, kommerziell | ✅ Vercel Pro |
| JAMstack-Seite, Kosteneffizienz | ⚖️ Netlify erwägen |
| Performance-kritisch, global | ⚖️ Cloudflare Pages erwägen |
| Vollständiger AWS-Stack | ⚖️ AWS Amplify |
| Persönliches Projekt, Lernen | ✅ Vercel Hobby |
| Enterprise, SSO/Compliance nötig | ✅ Vercel Enterprise (Budget prüfen) |

---

## 10. Fazit

Vercel hat sich als die führende Plattform für modernes Frontend-Hosting etabliert, insbesondere für Next.js-Anwendungen. Die Deep-Integration mit ihrem eigenen Framework, die exzellente Developer Experience und das starke Edge-Netzwerk machen es zur ersten Wahl für viele React-Teams.

**Stärken:**
- Unschlagbare Next.js-Integration
- Hervorragende Developer Experience
- Starkes AI-Produktportfolio (v0, AI SDK)
- Aktives Open Source-Engagement
- Schnelles globales Edge-Netzwerk

**Schwächen:**
- Preissprung zwischen Pro und Enterprise ($20k+)
- Bandbreiten-Kosten bei hohem Traffic
- Vendor Lock-in durch Ökosystem-Optimierung
- Nicht ideal für nicht-Frontend-Heavy-Workloads

**Gesamtbewertung:** Für moderne Frontend-Teams, besonders Next.js-Nutzer, ist Vercel die Referenz-Plattform. Für kostensensible oder Backend-heavy Projekte sollten Alternativen geprüft werden.

---

## Quellen

- Wikipedia: https://en.wikipedia.org/wiki/Vercel
- Vercel Pricing: https://vercel.com/pricing
- Vercel Docs: https://vercel.com/docs
- GitHub vercel/next.js: https://github.com/vercel/next.js
- GitHub vercel/vercel: https://github.com/vercel/vercel
- AI SDK Docs: https://sdk.vercel.ai/docs
- v0.dev: https://v0.dev
- Netlify Pricing: https://www.netlify.com/pricing/
- Forbes Guillermo Rauch Interview
- TechCrunch Funding News

---

*Recherche durchgeführt im Februar 2025. Preise und Features können sich ändern – aktuelle Daten direkt bei den Anbietern prüfen.*
