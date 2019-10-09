import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Exception;
import java.io.*;

public class Search {

	// etape 5
	public ArrayList<ArrayList<Integer>> searchWithDFA(DFA dfa, String txt, int idLine, int idCol) {
		// System.out.println("........ text : " + txt);
		int N = txt.length();
		ArrayList<String> resutls = new ArrayList<>();
		ArrayList<ArrayList<Integer>> resutlsF = new ArrayList<ArrayList<Integer>>();

		ArrayList<Integer> currentState = null;
		ArrayList<Integer> nextState = null;
		String currentChar = "";
		String maxMatching = "";
		HashMap<String, ArrayList<ArrayList<Integer>>> matchingWordId = new HashMap<String, ArrayList<ArrayList<Integer>>>();

		boolean change = false;
		System.out.println("0 je suis la !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		int idxCharFirstRep = -1;

		for (ArrayList<Integer> state : dfa.q0) {
			currentChar = "";

			for (int i = 0; i < N; i++) {
				System.out.println(i + "..............................................................");
				if (dfa.Transitions.containsKey(state)) {
					System.out.println(" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! dfa.Transitions.get(state)"  + dfa.Transitions.get(state).keySet());

					currentState = dfa.Transitions.get(state).get((int) txt.charAt(i));
					// System.out.println("$$ 1 je suis la !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					System.out.println("$$ i : " + txt.charAt(i));

					System.out.println("$$ currentState: " + currentState);
					if (currentState != null) {
						System.out.println(" 		dfa.Transitions.get(state)"  + dfa.Transitions.get(currentState).keySet());
						if(dfa.Transitions.get(currentState).keySet().contains('.'))
							System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

						System.out.println("		i+1 : " + txt.charAt(i+1));
						nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
						System.out.println("		 nextState: " + nextState);
						currentChar = currentChar + txt.charAt(i);
						// System.out.println(" currentChar: " + currentChar);

						if (dfa.f.contains(currentState) && nextState == null) {
							// System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
							// System.out.println(" nextState: " + currentState);

							currentChar = currentChar + txt.charAt(i);
							// System.out.println(" >>>> currentChar : " + currentChar);

							ArrayList<Integer> elem = new ArrayList<>();
							elem.add(idLine);
							//elem.add(i - currentChar.length() + 2); // deb
							elem.add(i+1);
							elem.add(i+currentChar.length()+1); // fin
							resutlsF.add(elem);
							resutls.add(currentChar);

							matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

							change = false;
							if (maxMatching.length() < currentChar.length())
								maxMatching = currentChar;
							currentChar = "";
						} else if (nextState != null && dfa.Transitions.containsKey(currentState) && i < N - 1) {
							System.out.println(" ---------------------------------------------------" + txt.charAt(i));
							if (!dfa.f.contains(currentState)) {

								currentChar = currentChar + txt.charAt(i + 1);
								// System.out.println(" 0 next txt: " + txt.charAt(i + 1));
								// System.out.println(" 0 currentChar : " + currentChar);
								change = false;
								// currentState = nextState;

								nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
								// System.out.println(" 0 nextState : " + nextState);
								currentState = nextState;
								i += 1;
								int oldI =i;

								while (nextState != null && i < N - 1) {

									if(dfa.q.contains(currentState) && currentChar.length() > 2) {
										System.out.println("		[DANS] currentChar : " + currentChar);

										if(idxCharFirstRep == -1)
											idxCharFirstRep = i;
									}
									System.out.println(">>>>>> : " + txt);
									System.out.println(" 0 idxCharFirstRep >>>>>> : " + idxCharFirstRep);

									// System.out.println("idxCharFirstRep >>>>>> : " + idxCharFirstRep);
									// System.out.println(" * txt: " + txt.charAt(i));
									// System.out.println(" * txt: (r) " + txt.charAt(i + 6));

									change = true;
									// System.out.println(" * current txt: " + txt.charAt(i));
									// System.out.println(" * next txt: " + txt.charAt(i + 1));
									// System.out.println(" AVANT currentState : " + currentState);
									// System.out.println(" 0 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" +
									// dfa.Transitions.get(currentState));

									if (dfa.Transitions.get(currentState) != null) {
										// System.out.println(" 1 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
										nextState = dfa.Transitions.get(currentState).get((int) txt.charAt(i + 1));
										// System.out.println(" New currentChar : " + currentState);
										// System.out.println(" NextStat : " + nextState);
									} else {
										// System.out.println("2 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
										nextState = null;
									}
									// System.out.println("3 je suis la!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

									if (nextState != null) {
										currentChar = currentChar + txt.charAt(i + 1);
										// System.out.println(" 1 next txt: " + txt.charAt(i + 1));
										// System.out.println(" + 1 currentChar: " + currentChar);
										currentState = nextState;
										// System.out.println(" + 1 currentState: " + currentState);
									} else {
										// System.out.println(" je break !!!");
										break;
									}
									i += 1;
								}
								i = oldI;
								// System.out.println(" Apres le while " + i);
								if (nextState != null && i == N - 1) {
									currentState = nextState;
									nextState = null;
								}
								// System.out.println(" >>>> nextState : " + nextState);

								if (nextState == null && change) {
									// System.out.println(" suite du break !!!");
									// System.out.println(" APRES le break currentChar: " + currentChar);
									i -= 1;
									// System.out.println(" >>>> currentState : " + currentState);
									// System.out.println(" >>>> nextState : " + nextState);
									// System.out.println(" >>>> dfa.f. : " + dfa.f);
									if (dfa.f.contains(currentState)) {
										// System.out.println(" je suis accepté : " + currentChar);
										System.out.println("txt.charAt(i); : " + txt.charAt(i));
										System.out.println("i : " + i);
										ArrayList<Integer> elem = new ArrayList<>();
										elem.add(idLine);
										elem.add(i+1);
										elem.add(i+currentChar.length()+1);

										resutlsF.add(elem);
										resutls.add(currentChar);
										matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

										currentChar = "";
										change = false;
										if (maxMatching.length() < currentChar.length())
											maxMatching = currentChar;
									} else {
										// System.out.println(" je ne suis pas accepté : " + currentChar + " | " +
										// currentState);
									}
									currentChar = "";
								}
							} else {
								// System.out.println(" 2 >>>> currentChar : " + currentChar);
								ArrayList<Integer> elem = new ArrayList<>();
								elem.add(idLine);
								// 1 : currentChar.length() = wordToSearch.length()-1
								// pour currentChar.length()- 1
								// 1 pour commencer de 1 et non de 0
								elem.add(i+1);
								elem.add(i+currentChar.length()+1);
								resutlsF.add(elem);
								resutls.add(currentChar);
								matchingWordId.computeIfAbsent(currentChar, k -> new ArrayList<>()).add((elem));

								change = false;
								if (maxMatching.length() < currentChar.length())
									maxMatching = currentChar;
								currentChar = "";
							}
						} else
							currentChar = "";

						// System.out.println("sinon RIEN");
					}
					currentChar = "";
					// System.out.println("je suis mammmmm: ");
				}
			}
		}
		/*
		 * if (resutls.size() != 0) { System.out.println("results: " + resutls);
		 * System.out.println("maxMatching in this line (texte): " + maxMatching); }
		 */
		// System.out.println(">>> resutls : " + resutls);
		System.out.println(">>> resutls : " + resutlsF);
		return resutlsF;
	}

	public int[] retenue(String str) {
		int[] retenue = new int[str.length()];
		char[] facteur = str.toCharArray();

		int currentIdx = 0;
		int value = 0;
		int j = 1;

		for (int i = 0; i < facteur.length; i++) {
			value = 0;
			if (facteur[i] == facteur[0]) {
				retenue[currentIdx] = -1;
				currentIdx += 1;
			} else if (currentIdx == 1) {
				currentIdx++;
				retenue[currentIdx] = 0;
			} else {
				j = 1;
				while (j <= currentIdx - j + 1) {
					if (str.substring(0, j).equals(str.substring(currentIdx - j, currentIdx))) {
						value = j;
					}
					j++;
				}

				retenue[currentIdx] = value;
				currentIdx += 1;
			}
		}
		return retenue;
	}

	public ArrayList<ArrayList<Integer>> matchingWords(char[] facteur, int[] retenue, char[] line, int idLine) {
		int i = 0;
		int j = 0;
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();

		while (i < line.length) {
			if (j == facteur.length) {

				ArrayList<Integer> elem = new ArrayList<>();
				elem.add(idLine);
				elem.add(i - facteur.length + 1);

				result.add(elem);
				j = 0;
			}

			if (line[i] == facteur[j]) {
				i++;
				j++;
			} else {
				if (retenue[j] == -1) {
					i++;
					j = 0;
				} else {
					j = retenue[j];
				}
			}

		}

		if (j == facteur.length) {
			ArrayList<Integer> elem = new ArrayList<>();
			elem.add(idLine + 1);
			elem.add(i - facteur.length + 1);

			result.add(elem);
		}
		return result;
	}

	public static String readFileChar(String file) throws FileNotFoundException {
		String result = "";
		try {
			FileReader fr = new FileReader(file);
			int i;
			while ((i = fr.read()) != -1)
				result += (char) i;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}