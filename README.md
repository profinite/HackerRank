# HackerRank-Solutions
Original solutions to HackerRank problems (LLM-free!)

Each solution passes all test cases currently online (as of commit time ~2022-present). 

Under my analysis, every algorithm has at least quasi-polynomial runtime complexity. A few demand quadratic solutions (𝚯(N²)) but otherwise all target a linear runtime or at least 𝚯(N log N). 

Space complexity is usually a secondary consideration: instead we focus on lucidity, often defining classes or data structures to more cogently illustrate the solution, or even investigate a rarely-used collection class. As an exercise, many solutions are also adapted specifically for **parallelization**, introducing streams and additional memory use. In practice, this usually only yields benefits around 8+ threads - comment out parallel() when running on the hackerrrank.com crucible.

Generally every file is left as-is once committed and passing, to capture my (highly imperfect) understanding of syntax for future comparison.

- [x] Upload _Data Structures_ solutions
- [x] Upload Medium Algorithm answers
- [ ] Finish the Hard Algorithm problems 
- [ ] Finish the Euler Project problems

(c)2022 Tyler C de Laguna <tyler@delaguna.org>

