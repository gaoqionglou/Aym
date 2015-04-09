package com.gao.aym.util;

import java.util.Comparator;

import com.gao.aym.bean.LetterContact;

/**
 *
 */
public class PinyinComparator implements Comparator<LetterContact> {

	public int compare(LetterContact o1, LetterContact o2) {
		
		if (o1.getSortletter().equals("@")
				|| o2.getSortletter().equals("#")) {
			return -1;
		} else if (o1.getSortletter().equals("#")
				|| o2.getSortletter().equals("@")) {
			return 1;
		} else {
			String str1=o1.getSortletter();
			String str2=o2.getSortletter();
			return str1.compareTo(str2);
		}
	}

}
