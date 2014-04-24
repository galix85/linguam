package com.galix.linguam.gameengine;

import java.util.ArrayList;

import com.galix.linguam.LinguamApplication;
import com.galix.linguam.R;
import com.galix.linguam.pojo.GameData;
import com.galix.linguam.pojo.PairWord;
import com.galix.linguam.pojo.TranslatedWord;

public class GameEngine {

	private static GameEngine instance = null;
	private static ArrayList<GameData> gameDataList;
	
	public void init(){
		setGameDataList(loadAllWords());
	}
	
	public static GameEngine getInstance() {
		if(instance != null) {
			return instance;
		} else {
			return new GameEngine();
		}
	}
		
	/**
	 * 
	 * @return
	 */
	public ArrayList<GameData> loadAllWords(){
		
		//TODO Filter per level
		//Loop num of iteration
		/*for (int i = 0; i <= iterationPerLevel; i++){
			// Get words per level, original word
			// Depends of level get 2, 4
		}*/
		//get pair
		ArrayList<GameData> gameDataList = new ArrayList<GameData>();
		ArrayList<PairWord> allTranslateWordList = LinguamApplication.translatedWordDB.getPairWord();
		for (PairWord pairWord : allTranslateWordList) {
			//fill padding words
			
			int nWords = 0;
			if (pairWord.getLevel() == 1 || pairWord.getLevel() == 2) {
				nWords = 1;
			}else if(pairWord.getLevel() == 3 || pairWord.getLevel() == 4){
				nWords = 3;
			}
			
			ArrayList<String> paddingWords = LinguamApplication.translatedWordDB.getPaddingWordList(pairWord.getOriginalWord(), nWords);

			//Set info to GameData object
			GameData gameData = new GameData();
			gameData.setPairWord(pairWord);
			gameData.setPaddingWordList(paddingWords);
			
			//Add to list of gameData
			gameDataList.add(gameData);
		}
		
		return gameDataList;
		
	}
	
	/**
	 * 
	 * @param checkedAnswer
	 * @param originalWord
	 * @return
	 */
	public boolean checkAnswer(String checkedAnswer, String originalWord){
		
		TranslatedWord translatedWord = LinguamApplication.translatedWordDB.getTranslateByWord(originalWord);
		String correctAnswer = translatedWord.getTerm();
		
		if (correctAnswer.toUpperCase().equals(checkedAnswer))
			return true;
		
		return false;
		
	}
	
		
	public void saveData(GameData gameData){
	}
	
	public void getData(GameData gameData){
	}
	
	public ArrayList<GameData> getGameDataList() {
		return gameDataList;
	}

	public void setGameDataList(ArrayList<GameData> gameDataList) {
		GameEngine.gameDataList = gameDataList;
	}

	
	
	
}
