import json
import os

FILE_NAME = 'dictionary.json'

DEFAULT_DICTIONARY = {
    "cat": "kissa",
    "dog": "koira"
}

def load_dictionary():
    if not os.path.exists(FILE_NAME):
        print("Dictionary file not found. Loading default dictionary.")
        return DEFAULT_DICTIONARY.copy()
    
    try:
        with open(FILE_NAME, 'r', encoding='utf-8') as file:
            return json.load(file)
    except json.JSONDecodeError:
        print("Error: The dictionary file is corrupted. Loading default dictionary.")
        return DEFAULT_DICTIONARY.copy()
    except Exception as e:
        print(f"An unexpected error occured while reading: {e}")
        return DEFAULT_DICTIONARY.copy()
    
def save_dictionary(dictionary):
    try:
        with open(FILE_NAME, 'w', encoding='utf-8') as file:
            json.dump(dictionary, file, indent=4)
            print("Dictionary saved succesfully.")
    except Exception as e:
        print(f"Error: Could not save the dictionary. {e}")
        
def main():
    print("Welcome to the Dictionary App!")
    my_dict = load_dictionary()

    while True:
        user_input = input("\nEnter a word to search (or press Enter to exit): ").strip().lower()

        if user_input == "":
            break

        if user_input in my_dict:
            print(f"Translation: {my_dict[user_input]}")
        else:
            print("Word not found. Please input a definition.")

            new_definition = input("Definition: ").strip().lower()

            if new_definition !="":
                my_dict[user_input] = new_definition
                print(f"Word '{user_input}' added to the dictionary!")
            else:
                print("No definition provided. Word was not added.")

    save_dictionary(my_dict)
    print("Goodbye!")

if __name__ == "__main__":
    main()
