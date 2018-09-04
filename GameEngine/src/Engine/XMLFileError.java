package Engine;

import java.io.Serializable;

public enum XMLFileError implements Serializable {

    FILE_DOES_NOT_EXIST{
        public String toString(){
            return "File does not exist :-(";
        }
    },
    FILE_IS_NOT_XML{
        public String toString(){
            return "The file is not in XML format, shame!";
        }
    },
    INVALID_NUM_OF_COLS{
        public String toString(){
            return "XML file has invalid number of rows. Must be between 5 to 50.";
        }
    },
    INVALID_NUM_OF_ROWS{
        public String toString(){
            return "XML file has invalid number of cols. Must be between 6 to 30.";
        }
    },
    INVALID_TARGET_ROWS{
        public String toString(){
            return "XML file has invalid target number. Must be less than the number of rows.";
        }
    },
    INVALID_TARGET_COLS{
        public String toString(){
            return "XML file has invalid target number. Must be less than the number of cols.";
        }
    },
    INVALID_TARGET_MIN{
        public String toString(){
            return "XML file has invalid target number. Must be more then one.";
        }
    },
    INVALID_NUM_OF_PLAYERS{
        public String toString(){
            return "XML file has invalid number of players. Must be between 2 to 6.";
        }
    },
    INVALID_PLAYERS_ID{
        public String toString(){
            return "XML file has invalid player Ids. Two players cannot have the same ID.";
        }
    },
    ALL_GOOD {
        public String toString(){
            return "Great file. Good for you :-)";
        }
    }

}
