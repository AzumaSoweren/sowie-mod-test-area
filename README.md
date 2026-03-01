![test-logo](assets/ui/sorc-test-logo.png)

---
A [___Mindustry___](https://github.com/Anuken/Mindustry) _kotlin mod_ that stores __experimental__ contents.

## Contents
### Fully-dynamic Algorithms for Connectivity
- It is implemented using the [___Holm, de Lichtenberg, Thorup___](https://dl.acm.org/doi/epdf/10.1145/502090.502095) _algorithm_.
- In the [`testarea/sorc/struct/graph`](src/testarea/sorc/struct/graph) folder, the core file is [`Graph.kt`](src/testarea/sorc/struct/graph/Graph.kt).

### Dual Kawase Blur
- More efficient than Gaussian blur.
- TODO: Fix flickering issue in cases where the scale is small.

### Other Data Structures
- May be more efficient than Arc.

## Warnings
- Contents in the repository are __experimental__ and __unstable__, which may cause your game to __crash__. _(If this happens, please report it.)_
- In the [`testarea/sorc`](src/testarea/sorc) folder, there are some tools that are __nearly__ perfected.
- ~~___TODO___~~

## Recommended Links
Here are some useful tools I'v used in modding.
- [Mindustry Text Generator](https://itzcraft.github.io/mindtext-gen/)
- [mod-tools](https://github.com/I-hope1/mod-tools/)

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.