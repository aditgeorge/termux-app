package com.termux.terminal;

import android.view.KeyEvent;


import java.util.HashMap;

import junit.framework.TestCase;

public class KeyHandlerTest extends TestCase {
    private static final int[] mod = {
        0,
        1,
        KeyHandler.KEYMOD_SHIFT,
        KeyHandler.KEYMOD_ALT,
        KeyHandler.KEYMOD_SHIFT | KeyHandler.KEYMOD_ALT,
        KeyHandler.KEYMOD_CTRL,
        KeyHandler.KEYMOD_SHIFT | KeyHandler.KEYMOD_CTRL,
        KeyHandler.KEYMOD_ALT | KeyHandler.KEYMOD_CTRL,
        KeyHandler.KEYMOD_SHIFT | KeyHandler.KEYMOD_ALT | KeyHandler.KEYMOD_CTRL
    };

	private static String stringToHex(String s) {
		if (s == null) return null;
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			if (buffer.length() > 0) {
				buffer.append(" ");
			}
			buffer.append("0x");
			buffer.append(Integer.toHexString(s.charAt(i)));
		}
		return buffer.toString();
	}

	private static void assertKeysEquals(String expected, String actual) {
		if (!expected.equals(actual)) {
			assertEquals(stringToHex(expected), stringToHex(actual));
		}
	}

	/** See http://pubs.opengroup.org/onlinepubs/7990989799/xcurses/terminfo.html */
	public void testTermCaps() {
		// Backspace.
		assertKeysEquals("\u007f", KeyHandler.getCodeFromTermcap("kb", false, false));

		// Back tab.
		assertKeysEquals("\033[Z", KeyHandler.getCodeFromTermcap("kB", false, false));

		// Arrow keys (up/down/right/left):
		assertKeysEquals("\033[A", KeyHandler.getCodeFromTermcap("ku", false, false));
		assertKeysEquals("\033[B", KeyHandler.getCodeFromTermcap("kd", false, false));
		assertKeysEquals("\033[C", KeyHandler.getCodeFromTermcap("kr", false, false));
		assertKeysEquals("\033[D", KeyHandler.getCodeFromTermcap("kl", false, false));
		// .. shifted:
		assertKeysEquals("\033[1;2A", KeyHandler.getCodeFromTermcap("kUP", false, false));
		assertKeysEquals("\033[1;2B", KeyHandler.getCodeFromTermcap("kDN", false, false));
		assertKeysEquals("\033[1;2C", KeyHandler.getCodeFromTermcap("%i", false, false));
		assertKeysEquals("\033[1;2D", KeyHandler.getCodeFromTermcap("#4", false, false));

		// Home/end keys:
		assertKeysEquals("\033[H", KeyHandler.getCodeFromTermcap("kh", false, false));
		assertKeysEquals("\033[F", KeyHandler.getCodeFromTermcap("@7", false, false));
		// ... shifted:
		assertKeysEquals("\033[1;2H", KeyHandler.getCodeFromTermcap("#2", false, false));
		assertKeysEquals("\033[1;2F", KeyHandler.getCodeFromTermcap("*7", false, false));

		// The traditional keyboard keypad:
		// [Insert] [Home] [Page Up ]
		// [Delete] [End] [Page Down]
		//
		// Termcap names (with xterm response in parenthesis):
		// K1=Upper left of keypad (xterm sends same "<ESC>[H" = Home).
		// K2=Center of keypad (xterm sends invalid response).
		// K3=Upper right of keypad (xterm sends "<ESC>[5~" = Page Up).
		// K4=Lower left of keypad (xterm sends "<ESC>[F" = End key).
		// K5=Lower right of keypad (xterm sends "<ESC>[6~" = Page Down).
		//
		// vim/neovim (runtime/doc/term.txt):
		// t_K1 <kHome> keypad home key
		// t_K3 <kPageUp> keypad page-up key
		// t_K4 <kEnd> keypad end key
		// t_K5 <kPageDown> keypad page-down key
		//
		assertKeysEquals("\033[H", KeyHandler.getCodeFromTermcap("K1", false, false));
		assertKeysEquals("\033OH", KeyHandler.getCodeFromTermcap("K1", true, false));
		assertKeysEquals("\033[5~", KeyHandler.getCodeFromTermcap("K3", false, false));
		assertKeysEquals("\033[F", KeyHandler.getCodeFromTermcap("K4", false, false));
		assertKeysEquals("\033OF", KeyHandler.getCodeFromTermcap("K4", true, false));
		assertKeysEquals("\033[6~", KeyHandler.getCodeFromTermcap("K5", false, false));

		// Function keys F1-F12:
		assertKeysEquals("\033OP", KeyHandler.getCodeFromTermcap("k1", false, false));
		assertKeysEquals("\033OQ", KeyHandler.getCodeFromTermcap("k2", false, false));
		assertKeysEquals("\033OR", KeyHandler.getCodeFromTermcap("k3", false, false));
		assertKeysEquals("\033OS", KeyHandler.getCodeFromTermcap("k4", false, false));
		assertKeysEquals("\033[15~", KeyHandler.getCodeFromTermcap("k5", false, false));
		assertKeysEquals("\033[17~", KeyHandler.getCodeFromTermcap("k6", false, false));
		assertKeysEquals("\033[18~", KeyHandler.getCodeFromTermcap("k7", false, false));
		assertKeysEquals("\033[19~", KeyHandler.getCodeFromTermcap("k8", false, false));
		assertKeysEquals("\033[20~", KeyHandler.getCodeFromTermcap("k9", false, false));
		assertKeysEquals("\033[21~", KeyHandler.getCodeFromTermcap("k;", false, false));
		assertKeysEquals("\033[23~", KeyHandler.getCodeFromTermcap("F1", false, false));
		assertKeysEquals("\033[24~", KeyHandler.getCodeFromTermcap("F2", false, false));
		// Function keys F13-F24 (same as shifted F1-F12):
		assertKeysEquals("\033[1;2P", KeyHandler.getCodeFromTermcap("F3", false, false));
		assertKeysEquals("\033[1;2Q", KeyHandler.getCodeFromTermcap("F4", false, false));
		assertKeysEquals("\033[1;2R", KeyHandler.getCodeFromTermcap("F5", false, false));
		assertKeysEquals("\033[1;2S", KeyHandler.getCodeFromTermcap("F6", false, false));
		assertKeysEquals("\033[15;2~", KeyHandler.getCodeFromTermcap("F7", false, false));
		assertKeysEquals("\033[17;2~", KeyHandler.getCodeFromTermcap("F8", false, false));
		assertKeysEquals("\033[18;2~", KeyHandler.getCodeFromTermcap("F9", false, false));
		assertKeysEquals("\033[19;2~", KeyHandler.getCodeFromTermcap("FA", false, false));
		assertKeysEquals("\033[20;2~", KeyHandler.getCodeFromTermcap("FB", false, false));
		assertKeysEquals("\033[21;2~", KeyHandler.getCodeFromTermcap("FC", false, false));
		assertKeysEquals("\033[23;2~", KeyHandler.getCodeFromTermcap("FD", false, false));
		assertKeysEquals("\033[24;2~", KeyHandler.getCodeFromTermcap("FE", false, false));
	}


    public void testDpadKeys() {
        //DPAD_CENTER
        assertKeysEquals("\015", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_CENTER, 0, false, false));
        //DPAD_UP
        assertKeysEquals("\033OA", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_UP, mod[0], true, false));
        assertKeysEquals("\033[A", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_UP, mod[0], false, false));
        assertKeysEquals("\033[1A", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_UP, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "A", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_UP, mod[i], false, false));
        }
        //--------
        //DPAD_DOWN
        assertKeysEquals("\033OB", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_DOWN, mod[0], true, false));
        assertKeysEquals("\033[B", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_DOWN, mod[0], false, false));
        assertKeysEquals("\033[1B", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_DOWN, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "B", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_DOWN, mod[i], false, false));
        }
        //--------
        //DPAD_RIGHT
        assertKeysEquals("\033OC", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_RIGHT, mod[0], true, false));
        assertKeysEquals("\033[C", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_RIGHT, mod[0], false, false));
        assertKeysEquals("\033[1C", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_RIGHT, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "C", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_RIGHT, mod[i], false, false));
        }
        //--------
        //DPAD_LEFT
        assertKeysEquals("\033OD", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_LEFT, mod[0], true, false));
        assertKeysEquals("\033[D", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_LEFT, mod[0], false, false));
        assertKeysEquals("\033[1D", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_LEFT, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "D", KeyHandler.getCode(KeyEvent.KEYCODE_DPAD_LEFT, mod[i], false, false));
        }
        //--------
        //MOVE_HOME
        assertKeysEquals("\033OH", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_HOME, mod[0], true, false));
        assertKeysEquals("\033[H", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_HOME, mod[0], false, false));
        assertKeysEquals("\033[1H", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_HOME, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "H", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_HOME, mod[i], false, false));
        }
        //--------
        //MOVE_END
        assertKeysEquals("\033OF", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_END, mod[0], true, false));
        assertKeysEquals("\033[F", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_END, mod[0], false, false));
        assertKeysEquals("\033[1F", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_END, mod[1], false, false));
        for (int i = 2; i < mod.length; i++) {
            assertKeysEquals("\033[1;" + i + "F", KeyHandler.getCode(KeyEvent.KEYCODE_MOVE_END, mod[i], false, false));
        }
        //--------
    }
    public void testFnKeys() {
        HashMap<Integer, String> fn_keys1 = new HashMap<Integer, String>();
        fn_keys1.put(KeyEvent.KEYCODE_F1, "P");
        fn_keys1.put(KeyEvent.KEYCODE_F2, "Q");
        fn_keys1.put(KeyEvent.KEYCODE_F3, "R");
        fn_keys1.put(KeyEvent.KEYCODE_F4, "S");

        for (HashMap.Entry f : fn_keys1.entrySet()) {
            String v = (String) f.getValue();
            int k = (int) f.getKey();
            assertKeysEquals("\033O" + v, KeyHandler.getCode(k, mod[0], true, false));
            assertKeysEquals("\033[1" + v, KeyHandler.getCode(k, mod[1], false, false));
            for (int i = 2; i < mod.length; i++) {
                assertKeysEquals("\033[1;" + i + v, KeyHandler.getCode(k, mod[i], false, false));
            }
        }
        HashMap<Integer, Integer> fn_keys2 = new HashMap<Integer, Integer>();
        fn_keys2.put(KeyEvent.KEYCODE_F5, 15);
        fn_keys2.put(KeyEvent.KEYCODE_F6, 17);
        fn_keys2.put(KeyEvent.KEYCODE_F7, 18);
        fn_keys2.put(KeyEvent.KEYCODE_F8, 19);
        fn_keys2.put(KeyEvent.KEYCODE_F9, 20);
        fn_keys2.put(KeyEvent.KEYCODE_F10, 21);
        fn_keys2.put(KeyEvent.KEYCODE_F11, 23);
        fn_keys2.put(KeyEvent.KEYCODE_F12, 24);
        for (HashMap.Entry f : fn_keys2.entrySet()) {
            int v = (int) f.getValue();
            int k = (int) f.getKey();
            assertKeysEquals("\033[" + v + "~", KeyHandler.getCode(k, mod[1], false, false));
            for (int i = 2; i < mod.length; i++) {
                assertKeysEquals("\033[" + v + ";" + i + "~", KeyHandler.getCode(k, mod[i], false, false));
            }
        }
    }

    public void testNumpadKeys() {
	    //NUMPAD_0
	    assertKeysEquals("0",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_0,KeyHandler.KEYMOD_NUM_LOCK,false,false));
	    for(int i=2;i<mod.length;i++) {
	        assertKeysEquals("\033O;"+i+"p",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_0,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
	    for(int i=2;i<mod.length;i++) {
	        assertKeysEquals("\033[2;"+i+"~", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_0,mod[i],false,false));
        }
	    //------------
        //NUMPAD_1
        assertKeysEquals("1",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_1,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"q",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_1,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OF", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_1,0,true,false));
        assertKeysEquals("\033[F", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_1,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"F", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_1,mod[i],false,false));
        }
        //------------
        //NUMPAD_2
        assertKeysEquals("2",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_2,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"r",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_2,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OB", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_2,0,true,false));
        assertKeysEquals("\033[B", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_2,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"B", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_2,mod[i],false,false));
        }
        //------------
        //NUMPAD_3
        assertKeysEquals("3",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_3,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"s",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_3,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033[6~", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_3,0,true,false));
        //------------
        //NUMPAD_4
        assertKeysEquals("4",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_4,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"t",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_4,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OD", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_4,0,true,false));
        assertKeysEquals("\033[D", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_4,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"D", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_4,mod[i],false,false));
        }
        //------------
        //NUMPAD_5
        assertKeysEquals("5",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_5,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"u",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_5,mod[i],false,true));
        }
        //------------
        //NUMPAD_6
        assertKeysEquals("6",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_6,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"v",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_6,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OC", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_6,0,true,false));
        assertKeysEquals("\033[C", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_6,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"C", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_6,mod[i],false,false));
        }
        //------------
        //NUMPAD_7
        assertKeysEquals("7",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_7,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"w",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_7,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OH", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_7,0,true,false));
        assertKeysEquals("\033[H", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_7,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"H", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_7,mod[i],false,false));
        }
        //------------
        //NUMPAD_8
        assertKeysEquals("8",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_8,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"x",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_8,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033OA", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_8,0,true,false));
        assertKeysEquals("\033[A", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_8,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[1;"+i+"A", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_8,mod[i],false,false));
        }
        //------------
        //NUMPAD_9
        assertKeysEquals("9",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_9,KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"y",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_9,KeyHandler.KEYMOD_NUM_LOCK | mod[i],false,true));
        }
        assertKeysEquals("\033[5~", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_9,0,true,false));
        //------------
    }

    public void testMiscKeys() {
        //SYSRQ
        assertKeysEquals("\033[32~", KeyHandler.getCode(KeyEvent.KEYCODE_SYSRQ, 0, false, false));

        //BREAK
        assertKeysEquals("\033[34~", KeyHandler.getCode(KeyEvent.KEYCODE_BREAK, 0, false, false));

//        assertNull(KeyHandler.getCode(KeyEvent.KEYCODE_ESCAPE, 0, false, false));
        //BACK
        assertKeysEquals("\033", KeyHandler.getCode(KeyEvent.KEYCODE_BACK, 0, false, false));

        //UP
        assertKeysEquals("\033[5~", KeyHandler.getCode(KeyEvent.KEYCODE_PAGE_UP, 0, false, false));

        //DOWN
        assertKeysEquals("\033[6~", KeyHandler.getCode(KeyEvent.KEYCODE_PAGE_DOWN, 0, false, false));

        //NUM_LOCK
        assertNull(KeyHandler.getCode(KeyEvent.KEYCODE_NUM_LOCK, 0, false, false));
        assertKeysEquals("\033OP", KeyHandler.getCode(KeyEvent.KEYCODE_NUM_LOCK, 0, false, true));

        //DEL
        assertKeysEquals("\u007F", KeyHandler.getCode(KeyEvent.KEYCODE_DEL, 0, false, true));
        assertKeysEquals("\033\u007F", KeyHandler.getCode(KeyEvent.KEYCODE_DEL, KeyHandler.KEYMOD_ALT, false, true));
        assertKeysEquals("\u0008", KeyHandler.getCode(KeyEvent.KEYCODE_DEL, KeyHandler.KEYMOD_CTRL, false, true));
        assertKeysEquals("\033\u0008", KeyHandler.getCode(KeyEvent.KEYCODE_DEL, KeyHandler.KEYMOD_ALT | KeyHandler.KEYMOD_CTRL, false, true));

        //SPACE
        assertNull(KeyHandler.getCode(KeyEvent.KEYCODE_SPACE, 0, false, true));
        assertKeysEquals("\0", KeyHandler.getCode(KeyEvent.KEYCODE_SPACE, KeyHandler.KEYMOD_CTRL, false, true));

        //TAB
        assertKeysEquals("\011", KeyHandler.getCode(KeyEvent.KEYCODE_TAB, 0, false, true));
        assertKeysEquals("\033[Z", KeyHandler.getCode(KeyEvent.KEYCODE_TAB, KeyHandler.KEYMOD_SHIFT, false, true));

        //ENTER
        assertKeysEquals("\r", KeyHandler.getCode(KeyEvent.KEYCODE_ENTER, 0, false, true));
        assertKeysEquals("\033\r", KeyHandler.getCode(KeyEvent.KEYCODE_ENTER, KeyHandler.KEYMOD_ALT, false, true));

        //NUMPAD_COMMA
        assertKeysEquals(",", KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_COMMA, 0, false, true));

        //Testing for undefined keyevent
        assertNull(KeyHandler.getCode(KeyEvent.KEYCODE_AT, 0, false, false));
    }

    public void testMiscKeys2() {
	    //INSERT
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[2;"+i+"~",KeyHandler.getCode(KeyEvent.KEYCODE_INSERT,mod[i],false,false));
        }

        //FORWARD_DEL
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[3;"+i+"~",KeyHandler.getCode(KeyEvent.KEYCODE_FORWARD_DEL,mod[i],false,false));
        }

        //NUMPAD_ENTER
        assertKeysEquals("\n",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_ENTER,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"M",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_ENTER,mod[i],false,true));
        }

        //NUMPAD_MULTIPLY
        assertKeysEquals("*",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_MULTIPLY,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"j",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_MULTIPLY,mod[i],false,true));
        }

        //NUMPAD_ADD
        assertKeysEquals("+",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_ADD,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"k",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_ADD,mod[i],false,true));
        }

        //NUMPAD_SUBTRACT
        assertKeysEquals("-",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_SUBTRACT,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"m",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_SUBTRACT,mod[i],false,true));
        }

        //NUMPAD_DIVIDE
        assertKeysEquals("/",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_DIVIDE,0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"o",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_DIVIDE,mod[i],false,true));
        }

        //NUMPAD_DOT
        assertKeysEquals("\033On",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_DOT, KeyHandler.KEYMOD_NUM_LOCK,false,true));
        assertKeysEquals(".",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_DOT, KeyHandler.KEYMOD_NUM_LOCK,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033[3;"+i+"~",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_DOT,mod[i],false,true));
        }

        //NUMPAD_EQUALS
        assertKeysEquals("=",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_EQUALS, 0,false,false));
        for(int i=2;i<mod.length;i++) {
            assertKeysEquals("\033O;"+i+"X",KeyHandler.getCode(KeyEvent.KEYCODE_NUMPAD_EQUALS,mod[i],false,true));
        }
    }

}
