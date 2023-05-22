# Opotato
This mod has two main purposes:

-To port other performance/useful mods that don't support 1.16.5 Forge

-To optimize/fix other mods and add new config settings to them

Mod Splitting Plan: https://github.com/MCTeamPotato/Opotato/issues/6

This mod has entered the LTS phase and no new content will be added unless you request and create a new "Feature Request" in the GitHub Issue. 1.18.2 is NOT supported.

If you have difficulty accessing GitHub, Discord (Kasualix#5415) would be also a good place.

(But potential mod conflicts, crash issues will still be supported).

————————————————————————————————————————————


## 1.16.5
### Included Mods
[Alternate Current](https://www.curseforge.com/minecraft/mc-mods/alternate-current)

[Smooth Menu](https://www.curseforge.com/minecraft/mc-mods/smoothmenu)

[Schwarz](https://www.curseforge.com/minecraft/mc-mods/schwarz)
### Other Features
Print modfile names when launching game (Mainly for collecting file info more easily when watching others' logs).
### Modification to other mods
#### Ars Nouveau
Support disabling Mana Gems in basic loot chests.
#### Blue Skies
Support enabling any mods generate structure features in the dimensions

Support configuring the gatekeeper house generation spacing

Disable its Internet connection behavior

Support disabling dimension nerf system
#### Cataclysm
Support configuring its structures generation spacing

Also fix its structure setting log error

Support configuring whether or not Ignitium Armor & Final Fractal & Infernal Forge & Monstrous Helm & Zweiender has durability.

Support configuring the cooldown ticks length of Void Core & Infernal Forge & Incinerator.
#### Cracker's Wither Storm Mod
Re-write the Wither Sickness Ticking Handler for huge performance increase..(NOTE: This is not fully tested and may has bugs. If you encounter that Wither Sickness effect can't apply to you properly, pls write down "mixin.opotato.witherstormmod=false" in opotato mixin properties config)

Add some performance config options for it:

Support configuring whether or not should the Wither Sickness ticking system only take effect on players.

Support disabling the Wither Sickness ticking system for monsters and animals.

Support reducing the rendering amount of Block Cluster to increase FPS.

Support killing the Wither Storm Mod entities when Command Block dies to increase TPS, disabled by default, inspired by Lag Removal mod.
#### Elenai Dodge 2
Disable its Internet connection behavior
#### Epic Fight
Add a workaround mixin to fix Epic Fight & Oculus incompatible crash.

Do note: this workaround is totally not the proper way to fix this crash, but the proper way may need a thousand times more effort. (See [here](https://github.com/MCTeamPotato/Opotato/commit/3e78ec4f035694ef03aa3da74170565568135147#commitcomment-112836590) for details).
#### Wildfire's Female Gender Mod
let the gender feature only take effect on PlayerEntity to avoid crash with Corpse mod (the player corpse)
#### Headshot
Overwrite its core codes. Solved a possible NPE problem of Headshot, provide localization for it and add fully configurable DING sound on Headshot. You can use resourcepack to modify the status message when headshot occurs.
#### LDLib
Tweak its JEI Plugin code to fix error log with ModernFix (See [#70](https://github.com/embeddedt/ModernFix/issues/70))
#### Modern UI
override its overwrite to Minecraft GUI Scale calculation to avoid strange Rubidium incompatibility.
#### Oculus
Disable its NEC detection.
#### Ost Overhaul
Hugely optimized codes.
#### Quark
Disable its tooltip improvement function for the items of Apotheosis and Immersive Armors to avoid display mistakes.
#### RandomPatches
Disable its Internet connection behavior.
#### Supplementaries
Disable its Internet connection behavior.
#### The Undergarden
Support configuring its structures generation spacing.

————————————————————————————————————————————


## 1.18.2
In 1.18.2, Opotato development is inactive and features are limited.
### Included mods
[Sync Potato Effects](https://www.curseforge.com/minecraft/mc-mods/sync-potato-effect)

[Potato NaN Health Fix](https://www.curseforge.com/minecraft/mc-mods/potato-nan-health-fix)


————————————————————————————————————————————


## 1.18.2 & 1.16.5 Universal features
### Included mods
[MixinTrace](https://www.curseforge.com/minecraft/mc-mods/mixintrace)

[Creative One-Punch](https://www.curseforge.com/minecraft/mc-mods/creative-one-punch)
### Other Features
Duplicate Entity UUID Fix(You don't need [DEUF](https://www.curseforge.com/minecraft/mc-mods/deuf-duplicate-entity-uuid-fix) or the DEUF feature of [RandomPatches](https://www.curseforge.com/minecraft/mc-mods/randompatches) anymore with this mod installed. And this mod will auto-disable these two mods' DEUF feature when they're installed. Difference: performance improvements on event listening & Don't spam log)

Add incompatible warnings for several mods when you incorrectly install them (Well the warnings in 1182 is quite limited as I'm not familiar with or interested in 1.18.2's mods ecology).

Support killing all types of entities in Wither Storm Mod when Command Block dies (when the Command Block dies, the player is teleported to the overworld, but the entities in Wither Storm's Bowels keep ticking for a long time, affecting TPS) (Disabled by default, inspired by the Lag Removal mod)

Support configuring the maximum number of spawned entities of the same type within a chunk (This is disabled by default)
### Modification to other mods
#### Citadel
Disable its Internet connection behavior
#### Inspirations
Auto-disable its customPortalColor feature to avoid Rubidium incompatibility.
#### Jump Over Fences
Hugely optimized codes.
#### Kiwi
Disable its Internet connection behavior.
#### Placebo
Disable its Internet connection behavior.
#### Quark
Remove its module loading log spam.

Disable its Internet connection behavior.
#### Xaero's World Map / Minimap
Disable its Internet connection behavior.


————————————————————————————————————————————


## Misc
Contact (Discord): Kasualix#5415

Sponsor: https://afdian.net/a/callmekall

My other projects: https://legacy.curseforge.com/members/potato_____boy/projects
