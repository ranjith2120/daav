# 1. Design personality

**Defining adjectives:** precise, sober, resilient, legible, operational.

**Brand impression:** A dependable planning instrument for examining costly infrastructure decisions. It treats the network and its evidence as the primary visual material.

During complex work, the interface feels calm, stable, and explicit: users can see the current baseline, temporary scenario changes, calculation status, and consequences without interpreting decoration. It must never feel promotional, playful, opaque, gaming-oriented, or like a generic analytics dashboard.

**Direction rationale:** Rural planners need to compare real network conditions and costs for sustained periods, so restrained surfaces and visible structure reduce cognitive load.

# 2. Color

Use CSS custom properties below, mapped to Tailwind semantic utilities and shadcn tokens. Do not introduce gradients, translucent panels, neon tones, or extra brand colors. The primary accent is a dark engineering teal, used for the current action and selection rather than broad decoration.

| Token | Light | Dark | Use | Justification |
| --- | --- | --- | --- | --- |
| `--background` | `#F8FAF8` | `#101715` | App canvas | Low-glare neutral supports long sessions. |
| `--surface` | `#FFFFFF` | `#18211E` | Panels, inputs, tables | Separates work areas without ornament. |
| `--surface-raised` | `#F1F5F2` | `#22302B` | Active panels, menus | Gives controlled hierarchy. |
| `--border` | `#C7D2CB` | `#496058` | Rules, control boundaries | Keeps dense engineering content scannable. |
| `--text-primary` | `#17211D` | `#E8F0EC` | Headings and body | High contrast for technical reading. |
| `--text-secondary` | `#4A5A52` | `#B9C8C0` | Metadata and supporting copy | Clearly subordinate but readable. |
| `--accent` | `#006B5B` | `#007C6A` | Primary actions, selected state | Teal signals deliberate work without visual noise. |
| `--accent-hover` | `#005448` | `#00917B` | Hover/pressed accent | Distinguishes action feedback. |
| `--success` | `#1F6B45` | `#55B47A` | Valid, recovered, connected | Familiar operational success signal. |
| `--warning` | `#8A5A00` | `#E5A93B` | Caution, planned state | Visible caution without alarming severity. |
| `--error` | `#B42318` | `#F27A70` | Failure, destructive actions | Clear failure state for infrastructure issues. |

Core contrast is verified using WCAG relative luminance: light primary `#17211D` on `#F8FAF8` is **15.74:1**; light secondary `#4A5A52` on `#F8FAF8` is **6.96:1**; white on light accent `#006B5B` is **6.45:1**. Dark primary `#E8F0EC` on `#101715` is **15.67:1**; dark secondary `#B9C8C0` on `#101715` is **10.46:1**; white on dark accent `#007C6A` is **5.13:1**. These all exceed AA for normal text. Do not place normal text over semantic color fills unless the exact contrast is checked; use a labeled chip with a neutral surface where uncertain.

Graph-only semantic tokens are consistent in every canvas, legend, route result, and simulation: default edge `#60756B` light / `#92A69D` dark; selected edge `#006B5B` / `#33B8A3`; optimized edge `#1F6B45` / `#55B47A`; failed edge `#B42318` / `#F27A70`; redundant edge `#557A9B` / `#78ABD6`; critical facility `#8A5A00` / `#E5A93B`; unreachable node `#6B7280` / `#9CA3AF`; proposed edge `#8A5A00` / `#E5A93B`; route overlay `#006B5B` / `#33B8A3`.

Failed edges use a 2px dashed stroke and “Disabled” label, proposed edges use a 2px dotted stroke and “Planned” label, redundant edges use a paired-line marker, critical nodes have a triangular alert marker, and unreachable nodes use a broken-link badge. This supplies non-color distinction. Borders remain 1px `--border`; focus is a 3px `#006B5B` or `#33B8A3` outer ring with 2px offset and never relies on a border-color change alone.

# 3. Typography

Use **IBM Plex Sans** for display and text, with **IBM Plex Mono** only for identifiers, numerical result grids, and algorithm steps. Both are available from [Google Fonts](https://fonts.google.com/specimen/IBM+Plex+Sans) and the [IBM Plex repository](https://github.com/IBM/plex), licensed under the SIL Open Font License 1.1. Self-host WOFF2 files for predictable offline operational use; fallback is `"Segoe UI", Roboto, Arial, sans-serif`. Mono fallback is `"Cascadia Mono", "SFMono-Regular", Consolas, monospace`.

Load Sans weights 400, 500, 600, and 700, and Mono weights 400 and 500. Apply `font-variant-numeric: tabular-nums lining-nums` to costs, counts, dates, table numerics, metric summaries, and algorithm output. Graph labels use Sans 500; node codes use Mono 500.

| Level | Size | Weight | Line height | Letter spacing | Usage |
| --- | --- | --- | --- | --- | --- |
| Display | `2rem` | 700 | 1.15 | `-0.02em` | Dashboard/project title only |
| H1 | `1.5rem` | 700 | 1.25 | `-0.015em` | Page title |
| H2 | `1.25rem` | 600 | 1.3 | `-0.01em` | Major panel heading |
| H3 | `1rem` | 600 | 1.4 | `0` | Section heading |
| H4 | `0.875rem` | 600 | 1.4 | `0.01em` | Group label |
| Body | `0.875rem` | 400 | 1.5 | `0` | Default text |
| Small | `0.8125rem` | 400 | 1.45 | `0` | Supporting text |
| Caption | `0.75rem` | 500 | 1.35 | `0.01em` | Metadata, chart labels |
| Button | `0.875rem` | 600 | 1 | `0` | Button label |
| Label | `0.8125rem` | 600 | 1.35 | `0.01em` | Form and table labels |

Use one H1 per page and sequential headings thereafter. Body copy has a 70ch maximum, except tooltips at 36ch and form help at 52ch. Tables use 14px body / 12px header and compact 40px rows; numeric cells are right-aligned, dates and identifiers are left-aligned. Truncate one-line table metadata with an accessible full-value tooltip; names may wrap to two lines. Graph labels never render below 11px, hide secondary labels below 55% zoom, and show full details on selection.

# 4. Spacing and layout

Base unit is 4px. Spacing scale: `1=4`, `2=8`, `3=12`, `4=16`, `5=20`, `6=24`, `8=32`, `10=40`, `12=48`, `16=64`, `20=80` px. Application content max-width is 1600px except canvas workspaces, which are full available width. Header is 56px. Sidebar is 248px, collapsed 64px. Standard page padding is 24px desktop; standard panel gap is 16px; section gap is 32px; form label-to-control gap is 6px and control-to-control gap is 16px.

| Breakpoint | Grid/gutters/padding | Sidebar | Dashboard | Graph editor | Tables/modals |
| --- | --- | --- | --- | --- | --- |
| 375–767px | 4 columns, 16px gutters, 16px padding | Off-canvas drawer | Single column | Canvas first, details bottom sheet | Horizontal scroll with frozen first column; modal width calc(100%-32px) |
| 768–1023px | 8 columns, 20px gutters, 20px padding | 64px icon rail | 2-column summaries | Canvas with 320px collapsible inspector | Scroll wrapper; modal max 640px |
| 1024–1439px | 12 columns, 24px gutters, 24px padding | 248px | 3:2 summary/chart layout | Canvas plus 360px inspector | Sticky header; modal max 720px |
| 1440px+ | 12 columns, 32px gutters, 32px padding | 248px | Contextual 3/6/3 columns | Canvas plus resizable 380–480px inspector | Sticky header; modal max 800px |

Use CSS grid for dashboard analytics and comparison results, flex rows for toolbars and field groups, and resizable split panes for canvas pages. The graph canvas has a minimum height of 560px desktop, 480px tablet, 420px mobile, and fills remaining viewport height after page chrome where possible. Inspector width changes must not cover canvas tools; on mobile it becomes a modal bottom sheet. This preserves meaningful graph manipulation rather than a decorative symmetric layout.

# 5. Shape and depth

Radius scale: `2px` for badges and graph overlays; `4px` for small controls, inputs, buttons, menus; `6px` for cards, panels, and dialogs; `999px` only for compact status dots. Never use large pill controls for normal actions.

Use a 1px solid `--border` for inputs, cards, tables, and section divisions. Use 2px only for focused controls and selected graph objects. Dividers are 1px `--border`, inset to panel padding; panels separate through background contrast and borders, not colored stripes. Light and dark themes preserve the same geometry and use their tokenized border.

Permitted shadows: `--shadow-overlay: 0 8px 24px rgb(23 33 29 / 0.14)` and `--shadow-menu: 0 4px 12px rgb(23 33 29 / 0.12)`. Dark mode uses alpha `0.36`. Use shadows only for dialogs, menus, tooltips, and inspector overlays; use borders for all permanent layout surfaces. Z-index: canvas 0, sticky header 20, graph controls 30, inspector overlay 40, dropdown 50, dialog 60, toast 70.

# 6. Components

### Shared control contract

Controls use 36px compact or 40px standard height, 12px horizontal padding, 4px radius, label typography, 16px icons, and 8px icon-text gap. Hover changes surface or accent tone in 120ms; focus uses the defined 3px ring; active changes background one token darker; disabled has 45% opacity, `not-allowed`, and no hover. Loading replaces leading icon with a 16px static-progress spinner, retains label/width, and sets `aria-busy`. At narrow widths, toolbar controls retain 40px targets and overflow into a labeled menu.

| Component | Default and states | Keyboard/validation/responsive behavior |
| --- | --- | --- |
| Primary button | Accent fill, white label; hover `--accent-hover`; destructive error fill is not applicable | Enter/Space activates; disabled/loading contract applies; text wraps only below 160px. |
| Secondary button | Surface fill, border, primary text; hover raised surface | Same keyboard behavior; used for non-primary page actions. |
| Ghost button | Transparent, primary text; hover raised surface | Same; icon-only version needs accessible name and tooltip. |
| Destructive button | Error fill, white label; hover uses darker error derived token | Requires confirmation for delete/reset; never sole action in dialog. |
| Text/numeric/search input | 40px, 10px horizontal padding, surface, border; error border plus error text below | Label is visible; numeric input has unit suffix and rejects invalid value on blur; search has clear action. |
| Select/multi-select | 40px input appearance; menu uses raised surface | Arrow keys, Enter, Escape; multi-select exposes selected count and removable labels. |
| Checkbox/radio/switch | 18px checkbox/radio, 36×20 switch | Space toggles; switch only for immediate binary preferences, not destructive states. |
| Tabs | 36px height, bottom 2px active accent rule | Left/right arrows move tab focus; mobile becomes horizontal scroll. |
| Dialog/menu/tooltip/toast | Dialog 480–800px, menu min 180px, tooltip max 36ch, toast 360px | Dialog traps focus; menu supports arrows/Escape; tooltip also opens on focus; toasts announce status and persist errors. |
| Card/panel/metric | Surface, 1px border, 6px radius, 16px padding; metric value uses Mono 600 | No hover elevation except clickable cards, which show raised surface and focus ring. |
| Data table/pagination | 40px rows, sticky header, 12px header labels, subtle hover row | Sortable header button; row selection checkbox; pagination has labeled first/prev/next/last. |
| Badge/filter/breadcrumb | Badge 22px high, 2px radius; filters use shared inputs | Filters remain visible as removable chips; breadcrumbs collapse middle segments on mobile. |
| Empty/loading/error | Empty: concise explanation plus one action; loading: structural skeleton; error: message, request ID if available, retry | Announce with `role=status` or `role=alert`; no decorative illustrations required. |
| Confirmation dialog | Warning/error icon, direct impact sentence, typed phrase for sample reset | Initial focus is Cancel; destructive action requires explicit confirmation. |

### Domain components

**Graph controls:** a 40px vertical control group at top-left: zoom in/out, fit, reset view, and legend toggle. Each has text tooltip and keyboard equivalent in a reachable “Canvas commands” menu. Controls use surface background, 1px border, 4px radius, and 8px internal gaps.

**Network nodes:** 36px default and 44px selected. Village is rounded square, hospital is square with cross symbol, school is pentagon, water facility is droplet-outline in square, power station is hexagon, warehouse is rectangle, distribution center is diamond. Shapes use neutral surface/fill with a type glyph, visible 1.5px outline, label beneath, and labeled handles only on focus/connection edit. Selected node gets 2px accent outline; critical node gets alert triangle; unreachable node gets 2px dashed outline plus broken-link badge. No state depends on color alone.

**Edges:** default is 1.5px solid; hover 2px; selection 3px; optimized and route overlays 3px with a text label in the legend and edge detail. Use a 24px transparent pointer target. Edge labels show cost at >=85% zoom and distance at >=115%; collision handling shifts label to the nearest free midpoint, then hides it while retaining selection details. Failed edges are dashed and never accepted by active calculations; redundant edges are double-line; proposed edges dotted.

**Node selection and edge configuration panels:** 360–480px inspector with title, type/status badge, immutable ID copy action, editable fields, related edges, and contextual actions. Save bar is sticky at panel bottom. Validation is inline and summary-linked; small widths use a full-screen sheet.

**Algorithm result panel:** top total in Mono, component count, selected edge table, and algorithm event log. Step state includes `Considering`, `Accepted`, `Rejected`, `Complete`; considered edge pulses once, accepted adopts optimized style, rejected becomes thin muted dashed. Provide Play, Pause, Next, Previous, Reset only when server returns step data. Play runs at 700ms per step, Next/Previous are 40px buttons, Reset restores pre-run overlay. “Show final result” skips animation immediately. Disconnected result shows forest components and isolated nodes, never a generic error.

**Scenario comparison and failure controls:** baseline column and simulation column use identical metric rows. The simulation side has an outlined `Simulated scenario` label and persistent disabled-edge count. Failure controls provide search, checkbox edge list, “Disable selected”, per-edge Restore, Restore all, Reset, and Save scenario. Before failure, show baseline state; during edits, show pending change marker; after recalculation, show affected facilities and changed connectivity; after restore, announce recovered state. This makes a temporary scenario unmistakable from stored operational status.

**Chart legend and route detail:** legends are outside charts when space permits and keyboard-focusable; each item toggles series visibility and uses shape/pattern in addition to color. Route detail shows metric, total distance, total cost, ordered stops, and edge list; unreachable result uses an error state with source/destination and affected component, not an empty card.

# 7. Iconography

Use [Phosphor Icons](https://phosphoricons.com/) for interface symbols. It is MIT licensed, has a controlled technical stroke language, and offers clear filled/regular variants without mixing libraries. Use Regular at 1.5px equivalent appearance for ordinary actions and Bold only for selected navigation or destructive confirmations. Standard icons are 16px, navigation 20px, inline 14px, and graph tool icons 18px. Maintain 8px icon-text spacing and center icons optically with text.

Buttons keep an icon left of text unless the action is directional, where it appears on the right. Icon-only controls require `aria-label` and a focus-triggered tooltip. Use custom node symbols only within the graph as defined in section 6; do not replace meaningful labels with symbols. Avoid emoji and mixed icon families.

# 8. Imagery and product visuals

The primary visual is the React Flow network workspace. Its background is `--background`, untextured and without dot grids. A faint, optional 32px square engineering grid may appear only during node placement, at 6% opacity, and is off in analysis mode. Canvas minimap is bottom-right, 160×100px, neutral, labeled “Network overview,” and can be hidden.

At under 55% zoom render node shapes and state markers only; at 55–84% show names; at 85% show edge cost; at 115% show both cost and distance. Keep a minimum 12px visual gap around labels. Selected paths use the route semantic line plus ordered numeric stop markers. Legends are anchored bottom-left, collapse into a menu on mobile, and use the complete style key, not color swatches alone.

Optimization comparison uses aligned result tables and the live overlay, not decorative diagrams. Failure views use side-by-side baseline/simulation canvas modes only at desktop widths; tablet/mobile toggle between views while retaining a pinned comparison summary. Recharts use thin strokes, neutral axes, units in axis labels, direct legend labels, and an adjacent accessible data table/download-free textual summary.

Documentation screenshots use 16:10 desktop framing, visible synthetic dataset label when applicable, 16px outer padding, and no device mockups. Empty states show a concise explanation and one relevant action. Loading uses skeleton rows/panels and a determinate algorithm progress bar when progress is known. Errors use the error token, plain language, and retry or recovery action. No stock photography is used.

# 9. Motion

Durations: fast `120ms`, standard `180ms`, slow `280ms`. Easing: `cubic-bezier(0.2, 0, 0, 1)` for enter/update and `cubic-bezier(0.4, 0, 1, 1)` for exit. There is no spring, bounce, overshoot, continuous decorative animation, or animated metric counting.

Buttons and inputs use fast color/border transitions. Menus and dialogs fade and translate 4px over standard duration. Node dragging moves directly with pointer, then settles instantly. Algorithm steps use a 180ms line-state transition and 700ms dwell only in autoplay; route recalculation briefly highlights changed edges for 280ms; failed edges transition to dashed/error in 180ms; chart series update in 180ms with no animated axes. Skeletons use a static neutral block or a slow opacity pulse only.

Provide “Show final result” and “Reduce motion” controls. With `prefers-reduced-motion: reduce`, all transitions are 0ms, autoplay defaults off, route/failure changes render directly, dialog/menu transform is removed, and skeleton pulse is static. Motion explains computation or state change, never delays access to results.

# 10. Page-by-page composition

### 1. Overview dashboard

**Goal:** assess current network health and choose the next investigation. Header has project selector, baseline/simulation state, refresh, and Load demonstration network when empty. Use a 3/6/3 grid: left project/network status and warnings, center wide health/cost/resilience view, right recent scenarios and critical infrastructure. Include calculated metric summaries, node-type/cost distribution, connectivity summary, and actionable links. Empty state offers sample initialization; loading skeleton mirrors the grid; errors retain last data and expose retry. Mobile stacks warnings, health, then history. Rationale: high-priority risk information is visible before secondary charts.

### 2. Network Builder

**Goal:** create and inspect the baseline graph. Header includes network name/revision, undo only if implemented, validation status, and Add node/Add connection actions. Full-width split workspace: dominant canvas, contextual inspector, compact filter/tool strip. Include node/edge lists as an accessible alternative beneath canvas. Empty canvas offers Add node and Load demonstration network. Mobile opens inspector as a sheet and keeps graph controls pinned. Rationale: graph manipulation needs more space than configuration forms.

### 3. Cost Optimization

**Goal:** calculate and compare minimum-cost forests. Header states active baseline/scenario and Save run. Top configuration bar has algorithm segmented control, cost metric explanation, Run optimization, and Show final result. Results use 5/7 grid: summary/component feasibility at left; accepted edge table and canvas overlay at right; comparison appears below only after both runs exist. Empty state explains required active edges; disconnected state shows forest details. Mobile stacks controls, result total, canvas, table. Rationale: decision inputs remain close to evidence and calculated output.

### 4. Fault Tolerance

**Goal:** inspect bridges, articulation points, and resilience risks before a simulation. Header includes baseline state and Start simulation. Main 7/5 grid places graph/criticality overlay first and prioritized critical elements table second, with guidance and component verification below. Filters cover facility type and risk category. Loading preserves table/canvas shape; no-criticality success uses a positive status with exact calculated scope. Mobile offers canvas then ordered findings. Rationale: spatial consequences and prioritized evidence are inspected together.

### 5. Distribution Routing

**Goal:** find and verify a route between active facilities. Header includes baseline/scenario indicator. A 4/8 grid puts source/destination/metric controls and route summary at left, and route canvas plus ordered stops at right. Include distance, construction cost, and capacity values as separately labeled facts; no capacity-constrained claim is made. Unreachable state names the selected endpoints and component condition. Mobile keeps selectors before the canvas and collapses stops into expandable list. Rationale: route inputs remain stable while the map communicates the path.

### 6. Failure Simulation

**Goal:** safely test multiple temporary disabled connections. Header persistently shows `Simulated scenario`, affected count, Reset, Save scenario, and Exit simulation. Three-pane desktop layout: failure edge controls 300px, canvas flexible, comparison inspector 380px. Baseline/simulation toggle and affected facilities remain visible. Loading marks recalculation without hiding current state; restore gives a status announcement. Mobile makes controls and comparison full-screen sheets and retains canvas as center workflow. Rationale: scenario status and recovery actions must never be mistaken for baseline edits.

### 7. Analytics

**Goal:** compare derived cost, connectivity, and resilience outcomes. Header has baseline/scenario and date/history filters. Use varied full-width visual sections: cost/resilience comparison, coverage/connectivity, bottleneck/failure outcomes, each paired with an accessible table. Use actual API series only, show no-data state per panel, and avoid repeating metric-card layouts. Mobile changes charts to one column with table disclosure below. Rationale: analytic relationships need labels and room, not dense decoration.

### 8. Dataset Management

**Goal:** validate and maintain stored network data. Header includes data revision and Validate network. Use tabs for Nodes, Connections, Validation, and Sample data. Each data tab has filter toolbar, compact table, and Create action; detail forms open in a side panel. Sample initialization/reset uses confirmation dialogs and calls data synthetic where applicable. Narrow view hides low-priority columns behind a Columns menu. Rationale: dense tabular maintenance benefits from stable, predictable controls.

### 9. Settings

**Goal:** configure defined preferences and learn algorithm behavior. Header has Settings and no global save unless settings are staged. Stacked bordered groups: Appearance (theme), Visualization (label/minimap/animation preference), Algorithm defaults (Kruskal or Prim), and Algorithm information. Settings save per group with a local success message. Mobile remains a single column. Rationale: infrequent configuration should not compete with analysis work.

# 11. Copy voice

Write in direct, calm, technically precise sentences. Use sentence case for headings, buttons, labels, and status. Prefer: `network`, `connection`, `baseline`, `scenario`, `active`, `disabled`, `component`, `route`, `cost`, `distance`, `recalculate`. Avoid: `magic`, `seamless`, `smart`, `best-in-class`, `optimize everything`, `click here`, and jargon without explanation. Use active voice and name the affected object.

Format numbers with locale-aware grouping and tabular alignment. Currency uses the configured project currency code and two decimals, for example `INR 450,000.00`; distance uses `12.4 km`; capacity includes its configured unit; dates use `27 Oct 2026, 14:30 UTC`. Error messages use `[What happened]. [How to resolve it.]`, for example, `Connection cannot be saved. Select two different locations.` Form labels name the field, help text gives constraint, and icon controls have action labels.

Exactly five example headlines:

1. `Network health`
2. `Minimum-cost forest`
3. `Critical connections`
4. `Route to selected destination`
5. `Simulation impact`

Exactly five example button labels:

1. `Run Kruskal analysis`
2. `Find shortest route`
3. `Disable selected connections`
4. `Restore all connections`
5. `Load demonstration network`

# 12. Accessibility rules specific to this design

Meet the specified WCAG 2.2 AA implementation requirements: normal text contrast is at least 4.5:1, large text and non-text boundaries at least 3:1, 24×24px minimum pointer targets and 40×40px targets for primary touch controls. Use native buttons, inputs, labels, tables, headings, lists, and landmarks before ARIA. Do not use placeholder text as labels.

Keyboard order follows sidebar, header, page controls, then content. `Tab`/`Shift+Tab` move focus, Enter/Space activate, arrows operate tabs/radios/menus, Escape closes overlays, and focus returns to the invoking control. All focusable elements use the 3px accent focus ring with 2px offset. Dialogs set initial focus to the least destructive action, trap focus, expose title/description, and restore focus at close. Validation links an error summary to invalid fields through `aria-describedby`; async success/loading uses `role=status`; failed actions use `role=alert` without stealing focus.

Charts have visible title, unit, legend, keyboard series toggles, and an adjacent semantic data table. Tooltips are supplementary only and appear on focus as well as hover. Respect reduced-motion rules in section 9. Keep minimum body text at 14px, captions at 12px, and allow browser zoom to 200% without losing actions or requiring two-dimensional page scrolling except inside a labeled data-table/canvas viewport.

The graph canvas exposes a named region and accessible summary: node count, active/disabled edge count, component count, active overlays, and current zoom. It includes a keyboard-accessible Node list and Edge list with filters, sort, selection, inspect, edit, disable/restore, and route/optimization participation information. Canvas keyboard commands are documented in the Graph controls menu: arrow keys pan, `+`/`-` zoom, `f` fits, and Enter opens selected item details. Every node/edge has a programmatic name containing code, name/type or endpoints, operational state, and non-color state marker. Failed, planned, critical, unreachable, route, and optimized states are written in text and represented by the visual patterns defined earlier. This list-based equivalent is required when pointer graph interactions cannot be made fully keyboard-operable.

# 13. Assumptions and open questions

| Assumption | Chosen default | Why appropriate | Implementation impact |
| --- | --- | --- | --- |
| Responsive navigation | Persistent 248px sidebar at desktop, 64px tablet rail, modal drawer on mobile | Keeps analysis workspace large while preserving route access | App shell breakpoints and focus-managed drawer |
| Light and dark themes | System preference initially, explicit settings override persisted locally | Both are requested and operational environments vary | Tokenized Tailwind/shadcn themes |
| Data density | Compact 40px tables with configurable visible columns | Network management involves repeated inspection of many records | Table column and overflow controls |
| Project selection | A project/revision selector is shown whenever more than one saved network can exist | The PRD requires baseline revisions and history, while project entity details are not defined | Selection is a UI context abstraction until API model is finalized |
| Route capacity display | Display edge capacity as information only | Capacity-constrained routing is explicitly out of scope | Do not filter or rank Dijkstra paths by capacity |
| Planned links | Excluded from active analysis and shown as dotted proposed connections | Matches PRD release-1 active graph scope | Filter and legend semantics required |
| Dark graph canvas | Use dark app background, not black, with higher-lightness neutral edges | Preserves node labels and edge distinction in low light | Theme-specific graph token mapping |
| Import | No file import workflow beyond sample initialization and CRUD | The PRD does not define supported import formats | Dataset page must not expose CSV/upload controls |
| Algorithm animation | Optional only when server supplies step data | The PRD requires results, not a persisted step stream | Final result is always available immediately |
