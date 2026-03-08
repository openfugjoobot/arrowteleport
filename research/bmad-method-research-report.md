# BMAD Method - Strukturierter Recherche-Report

## 1. Was ist die BMAD Method?

### Definition
**BMAD** steht für **"Breakthrough Method of Agile AI-Driven Development"** (auch: **"Build More Architect Dreams"**).

Die BMAD Method ist ein **KI-gestütztes Entwicklungsframework**, das strukturierte, agentenbasierte Softwareentwicklung ermöglicht. Im Gegensatz zu herkömmlichem "Vibe Coding" (unstrukturiertes Prompting) bietet BMAD einen systematischen Ansatz mit definierten Rollen, Workflows und Artefakten.

### Kernprinzipien

| Prinzip | Beschreibung |
|---------|--------------|
| **12+ Spezialisierte Agenten** | Jeder Agent übernimmt eine definierte Rolle (Analyst, PM, Architekt, Scrum Master, Dev, QA, UX, etc.) |
| **Kontext-Engineering** | Detaillierte Story-Files mit vollständigem architektonischen Kontext |
| **Scale-Domain-Adaptive** | Automatische Anpassung der Planungstiefe basierend auf Projektgröße |
| **Git-basierte Versionierung** | Alle Artefakte (PRDs, Architektur, Stories) werden versioniert |
| **Human-in-the-Loop** | Menschliche Überwachung und Kontrolle bleiben zentral |

### Entwicklungsworkflow

```
Idee → Analyst → PM (PRD) → Architect → UX (optional) 
   ↓
PO (Master Checklist) → Sharding → SM (Story Prep) 
   ↓
Dev (Implementierung) → QA (Tests) → Merge
```

---

## 2. Offizielle GitHub-Repositories

### Haupt-Repository

| Repository | Sterne | Forks | Letztes Update | Lizenz |
|------------|--------|-------|----------------|--------|
| **[bmad-code-org/BMAD-METHOD](https://github.com/bmad-code-org/BMAD-METHOD)** | ⭐ 38.443 | 🍴 4.600+ | Feb 2026 | MIT |

### Module und Erweiterungen (bmad-code-org)

| Repository | Sterne | Beschreibung |
|------------|--------|--------------|
| **bmad-module-game-dev-studio** | 70 | Game Development Workflows (Unity, Unreal, Godot) |
| **bmad-builder** | 53 | BMad Builder - Erstellen eigener Agents/Workflows |
| **bmad-method-test-architecture-enterprise** | 28 | Risk-basierte Teststrategie und Automatisierung |
| **bmad-method-wds-expansion** | 24 | Web Development Studio Erweiterung |
| **bmad-module-creative-intelligence-suite** | 33 | Kreativität & Design Thinking |
| **bmad-bundles** | 10 | Bundle-Sammlungen |
| **BMAD-CORE** | 1 | Kern-Engine (Collaboration Optimized Reflection Engine) |
| **bmad-method-vscode** | 6 | VSCode-Integration |
| **bmad-cyber-sec** | 3 | Cyber Security Modul |
| **bmad-utility-skills** | 4 | Utility Skills Sammlung |
| **Full-Small-App-Workflow** | 3 | Kleine App Workflows |
| **bmad-plugins-marketplace** | 3 | Plugins Marketplace |
| **BMAD-FOUNDRY** | 1 | BMAD Foundry |

### Community Forks und Ports

| Repository | Beschreibung |
|------------|--------------|
| **bmadcode/BMAD-METHOD-v5** | Offizielle V5-Version |
| **24601/BMAD-AT-CLAUDE** | Portierung für Claude Code |
| **macelik/bmad-method** | Community Fork |
| **choisukjune/bmad-method** | Community Fork |
| **Samadaeus/bmad-method** | Community Fork |
| **bdebon/bmad-poc** | Demo-Projekt für Benjamin Code's Video |

### GitHub Topics
- [`bmad-method`](https://github.com/topics/bmad-method) - Wird von mehreren Projekten verwendet

---

## 3. Haupt-Autoren und Advocates

### Creator

| Name | Rolle | Links |
|------|-------|-------|
| **Brian "BMad" Madison** | Creator & Maintainer der BMAD Method | [LinkedIn](https://www.linkedin.com/in/briantmadison/) |

Brian Madison ist der offizielle Schöpfer und präsentiert die Methode in seinem YouTube-Kanal **[@BMadCode](https://www.youtube.com/@BMadCode)**.

### Organisation
- **bmad-code-org** - Offizielle GitHub-Organisation
- **BMad Code, LLC** - Rechtliche Entität (Trademark-Holder)

### Community Advocates

| Name | Beitrag | Quelle |
|------|---------|--------|
| Vishal Mysore | Umfassende Medium-Artikel & Tutorials | [Medium](https://medium.com/@visrow) |
| Steve Kaplan AI | Praxis-Anwendungen | Medium |
| Fab Dubin | Erfahrungsberichte | Medium |
| Benny Cheung | Enterprise Anwendung (Legacy Modernization) | [Blog](https://bennycheung.github.io/bmad-reclaiming-control-in-ai-dev) |
| Marius Sabaliauskas | Framework-Vergleiche | Medium |

---

## 4. Framework, Methodik oder Tool?

### Klassifizierung: **AI-Driven Development Framework**

Die BMAD Method ist ein umfassendes Framework, das mehrere Ebenen abdeckt:

```
┌─────────────────────────────────────────────┐
│           BMAD METHOD FRAMEWORK            │
├─────────────────────────────────────────────┤
│  EBNEBE: Methodik                           │
│  - Rollenbasierte Agenten-Workflows        │
│  - Agile Entwicklungsprozess               │
│  - Strukturierte Artefakte                 │
├─────────────────────────────────────────────┤
│  MITTEL: Tool                               │
│  - `npx bmad-method install`               │
│  - CLI-Installer                            │
│  - Codebase Flattener                       │
│  - VSCode-Erweiterungen                     │
├─────────────────────────────────────────────┤
│  UNTEN: Framework                           │
│  - YAML-basierte Agent-Definitionen         │
│  - Markdown-basierte System Prompts         │
│  - Modular erweiterbar (BMad Builder)     │
│  - Plugin-Ökosystem                        │
└─────────────────────────────────────────────┘
```

### Hauptkomponenten

1. **.bmad-core/** - Verstecktes Verzeichnis mit Agent-Definitionen
2. **Agents** - Markdown-Dateien mit YAML-Blöcken für Rollen
3. **Workflows** - YAML-basierte Prozessdefinitionen
4. **Story Files** - Kontext-reiche Entwicklungsanweisungen
5. **Templates** - Standardisierte Dokumente (PRDs, Architektur)

---

## 5. Praktische Anwendung

### Installation

```bash
# Projekt erstellen
mkdir mein-projekt
cd mein-projekt

# BMAD installieren
npx bmad-method install

# Für CI/CD (nicht-interaktiv)
npx bmad-method install --directory /path/to/project --modules bmm --tools claude-code --yes
```

### Unterstützte IDEs/Tools

| Tool | Status | Empfohlene Agent-Integration |
|------|--------|------------------------------|
| **Claude Code** | ✅ Empfohlen | Native Unterstützung |
| **Cursor** | ✅ Unterstützt | IDE-Integration |
| **VS Code** | ✅ Unterstützt | Erweiterung verfügbar |
| **Codex CLI** | ✅ Unterstützt | OpenAI's CLI |
| **GitHub Copilot** | ⚠️ Eingeschränkt | Keine volle Agent-Unterstützung |

### Agenten-Command Beispiele

```
*help                    → Zeigt verfügbare Commands
*brainstorm {topic}      → Brainstorming-Session
*create-project-brief    → Erstellt Projektkurzbeschreibung
*create-prd               → Erstellt Product Requirements Document
*create-architecture      → Erstellt Architekturdokument
*create-story             → Erstellt User Story
*shard-docs               → Teilt Dokumente für Dev-Teams
*party-mode               → Multi-Agent Kollaboration
```

### Codebase Flattener Tool

```bash
# Standard (aktuelles Verzeichnis)
npx bmad-method flatten

# Benutzerdefinierter Pfad
npx bmad-method flatten --input /path/to/source --output code.xml
```

### Anwendungsbereiche (laut bmadcods.com)

- **Software-Entwicklung** (Full-Stack, Microservices)
- **Business Strategie** (Marktanalyse, Planung)
- **Creative Content** (Storyboarding, Scripting)
- **Personal Wellness** (Journaling, Habit Tracking)
- **Game Development** (Unity, Unreal, Godot)
- **Cyber Security** (Sicherheitsanalysen)

---

## 6. Community-Aktivität

### GitHub-Metriken (Haupt-Repo: bmad-code-org/BMAD-METHOD)

| Metrik | Wert | Bewertung |
|--------|------|-----------|
| **⭐ Stars** | ~38.400 | ⭐⭐⭐⭐⭐ Sehr hoch |
| **🍴 Forks** | ~4.600 | ⭐⭐⭐⭐⭐ Sehr aktiv |
| **Contributors** | Mehrere Dutzend | ⭐⭐⭐⭐ Aktiv |
| **Issues** | Aktiv verwaltet | ⭐⭐⭐⭐ Gut |
| **Pull Requests** | Regelmäßig | ⭐⭐⭐⭐ Gut |

### Aktivitätstimeline

- **Feb 2026** - Aktuelles Update (letzter Stand)
- **Aug 2025** - V6 Alpha Release
- **2025** - V4-V5 Development Phase
- **Sep 2025** - Medium-Artikel von Vishal Mysore
- **Okt 2025** - Enterprise Case Study (Benny Cheung)

### Community-Plattformen

| Plattform | URL | Aktivität |
|-----------|-----|-----------|
| **Discord** | [discord.gg/gk8jAdXWmj](https://discord.gg/gk8jAdXWmj) | Aktive Community |
| **GitHub Discussions** | bmad-code-org/BMAD-METHOD | Support & Ideen |
| **YouTube** | @BMadCode | Tutorials & Masterclass |
| **Medium** | Mehrere Autoren | Artikel & Tutorials |
| **npm Registry** | bmad-method | ~5.600+ Downloads/Woche |
| **Buy Me a Coffee** | buymeacoffee.com/bmad | Community-Support |

### Versionen

| Version | Status | Features |
|---------|--------|----------|
| **V6** | Aktuell (Alpha) | Cross-Platform Agent Team, Sub-Agent Inclusion, BMad Builder v1, Dev Loop Automation |
| **V5** | Stabil | 21+ Agents, 50+ Workflows |
| **V4** | Legacy | Grundlegende Funktionalität |

---

## 7. Alternative Methoden/Frameworks

### Direkte Vergleiche

| Framework | Ansatz | Best für | Sterne |
|-----------|--------|----------|--------|
| **BMAD Method** | Agentic Agile, strukturierte Workflows | Teams, Enterprise, komplexe Projekte | ⭐ 38.4k |
| **GitHub Spec Kit** | Spec-driven, einfacher | Einzelentwickler, klare Spezifikationen | ~500+ |
| **Taskmaster** | Task-basiert, CLI-fokussiert | Einzelentwickler, einfache Aufgaben | ~2k |
| **Agent OS** | All-in-One Agent System | Vielseitige Anwendungen | ~1.5k |

### Andere Agentic AI Frameworks (2025-2026)

| Framework | Entwickler | Fokus | Status |
|-----------|------------|-------|--------|
| **LangGraph** | LangChain | Workflow-Orchestrierung | ⭐ Sehr populär |
| **Agno** | Community | Leichtgewichtige Agents | ⭐ Wachsend |
| **SmolAgents** | Hugging Face | Einfache Agent-Erstellung | ⭐ Bekannt |
| **Mastra** | Mastra | TypeScript-native Agents | ⭐ Neu |
| **Pydantic AI** | Pydantic | Strukturierte AI-Apps | ⭐ Bekannt |
| **Atomic Agents** | BrainBlend-AI | Developer-Centric | ⭐ 3.5k+ |
| **AutoGen** | Microsoft | Multi-Agent Systeme | ⭐ Sehr populär |
| **CrewAI** | CrewAI | Multi-Agent Teams | ⭐ Sehr populär |

### Vergleich: BMAD vs. Alternativen

| Kriterium | BMAD | Spec Kit | LangGraph | AutoGen |
|-----------|------|----------|-----------|---------|
| **Einarbeitung** | Mittel | Niedrig | Hoch | Mittel |
| **Team-Größe** | Alle Größen | Einzel | Alle | Alle |
| **Struktur** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Flexibilität** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Enterprise-Ready** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| **Community** | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Dokumentation** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |

---

## 8. Bewertung: Reifegrad, Community-Größe, Aktivität

### Gesamtbewertung

```
┌─────────────────────────────────────────────────────────────┐
│                    BMAD METHOD SCORECARD                   │
├─────────────────────────────────────────────────────────────┤
│ Reifegrad:         ████████████████████████░░░░  85%        │
│ Community-Größe:   ████████████████████████░░░░  90%        │
│ Aktivität:         █████████████████████████░░░  88%        │
│ Dokumentation:     ██████████████████████████░░░  92%        │
│ Enterprise-Ready:  ████████████████████████░░░░  85%        │
├─────────────────────────────────────────────────────────────┤
│ GESAMT:            █████████████████████████░░░  88/100     │
└─────────────────────────────────────────────────────────────┘
```

### Reifegrad-Analyse

| Aspekt | Bewertung | Anmerkung |
|--------|-----------|-----------|
| **Code-Qualität** | ⭐⭐⭐⭐ | Aktive Entwicklung, gut strukturiert |
| **API-Stabilität** | ⭐⭐⭐⭐ | V6 ist stabil, aber noch in Alpha |
| **Test-Abdeckung** | ⭐⭐⭐ | Test Architect Modul vorhanden |
| **Rückwärtskompatibilität** | ⭐⭐⭐⭐ | Migrationspfade dokumentiert |

### Community-Analyse

- **Größe**: Sehr groß (38k+ Stars, 4.6k+ Forks)
- **Wachstum**: Wachsend (V6 Release hat neue Aufmerksamkeit gebracht)
- **Engagement**: Hoch (Discord aktiv, regelmäßige Medium-Artikel)
- **Diversität**: International (Englisch, teilweise Chinesische Forks)

### Stärken

1. 🏆 **Höchste GitHub-Sterne** unter agentischen Entwicklungsframeworks
2. 📚 **Umfassende Dokumentation** (docs.bmad-method.org)
3. 🎓 **Aktive Bildung** (YouTube Masterclass, Medium-Artikel)
4. 🔄 **Kontinuierliche Entwicklung** (regelmäßige Updates)
5. 🆓 **Open Source & kostenlos** (MIT-Lizenz, keine Paywalls)

### Schwächen

1. ⏳ **Lernkurve** - Erfordert Einarbeitung in Agentic-Workflows
2. 🔧 **Tool-Abhängigkeit** - Am besten mit Claude Code/Cursor
3. 🌐 **Englisch-lastig** - Wenig lokalisierung
4. 🧪 **V6 Alpha** - Hauptversion noch nicht final

---

## 9. Fazit und Empfehlung

### Für Wen ist BMAD geeignet?

| Profil | Empfehlung | Begründung |
|--------|------------|------------|
| **Solo-Entwickler** | ✅ Gut | Struktur reduziert Chaos |
| **Kleine Teams** | ✅⭐ Empfohlen | Skaliert gut |
| **Enterprise-Teams** | ✅⭐⭐ Stark empfohlen | Governance, Compliance |
| **AI-Neulinge** | ⚠️ Mittel | Lernkurve vorhanden |
| **Erfahrene Agent-Nutzer** | ✅⭐⭐ Sehr empfohlen | Besonders effektiv |

### Vergleich mit "Vibe Coding"

```diff
+ BMAD bietet Struktur, Nachvollziehbarkeit, Skalierbarkeit
- Reiner Vibe-Coding ist unstrukturiert, schwer wartbar
+ BMAD ist für Teams optimiert
- Vibe Coding funktioniert nur für Solo-Projekte
```

### Links für den Einstieg

- 🌐 [Offizielle Dokumentation](https://docs.bmad-method.org)
- 💻 [GitHub Repository](https://github.com/bmad-code-org/BMAD-METHOD)
- 🎥 [YouTube Masterclass](https://www.youtube.com/watch?v=LorEJPrALcg)
- 💬 [Discord Community](https://discord.gg/gk8jAdXWmj)
- 📦 [npm Package](https://www.npmjs.com/package/bmad-method)
- 🌐 [Website](https://bmadcodes.com)

---

## Quellenverzeichnis

1. [BMAD-METHOD GitHub](https://github.com/bmad-code-org/BMAD-METHOD)
2. [BMAD Method Documentation](https://docs.bmad-method.org)
3. [BMad Codes Website](https://bmadcodes.com)
4. [Vishal Mysore - Medium Artikel](https://medium.com/@visrow/what-is-bmad-method-a-simple-guide-to-the-future-of-ai-driven-development-412274f91419)
5. [Benny Cheung - Applied BMAD](https://bennycheung.github.io/bmad-reclaiming-control-in-ai-dev)
6. [GitHub Spec Kit vs BMAD Comparison](https://medium.com/@visrow/github-spec-kit-vs-bmad-method-a-comprehensive-comparison-part-1-996956a9c653)
7. [Framework Comparisons - Medium](https://medium.com/data-science-collective/agentic-ai-comparing-new-open-source-frameworks-21ec676732df)

---

*Bericht erstellt am: 28. Februar 2026*
*Recherche-Status: Vollständig*
