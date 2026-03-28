import re
import requests
from bs4 import BeautifulSoup

def load_contents(url):
    own_headers = {
        'User-Agent': 'OwnStudentProject/0.1'
    }
    try:
        response = requests.get(url, headers=own_headers)
        if response.status_code == 200:
            return response

    except Exception as e:
        print(f"An unexpected error occured while loading the URL: {e}")
        return
    
def parse_html(content):

    try:
        soup = BeautifulSoup(content.text, 'html.parser')
        return soup
    except Exception as e:
        print(f"The HTML couldn't be parsed: {e}")
        return

def save_content(content, is_html):
    save_path = input("Give me a valid path to save the contents?")
    try:

        if is_html:
            mode = "w"
            data = content.text
            encoding = "utf-8"
        else:
            mode = "wb"
            data = content.content
            encoding = None

        with open(save_path, mode, encoding=encoding) as file:
            file.write(data)

        print(f"Saving succeeded to: {save_path}")
        return
        
    except Exception as e:
        print(f"Saving failed: {e}")
        return

def check_dwords(soup):
    text_content = soup.get_text(separator=' ')

    words = ["bomb", "kill", "murder", "terror", "terrorist", "terrorists", "terrorism"]
    pattern = r'\b(' + '|'.join(words) + r')\b'

    matches = re.findall(pattern, text_content, flags=re.IGNORECASE)
    return len(matches)

def main():

    while True:
        user_input = input("Give me a valid URL to download?")
        if user_input == "":
            break

        else:
            content = load_contents(user_input)
            if content is None:
                print(f"Error opening url: {user_input}")
                continue

        encoding = content.encoding.lower() if content.encoding else ''
        content_type = content.headers.get('Content-Type', '').lower()

        is_html = 'text/html' in content_type or 'text/plain' in content_type
        is_utf = 'utf-8' in content_type or 'utf-8' in encoding

        if is_html and is_utf:
            soup = parse_html(content)
            dword_count = check_dwords(soup)
            print(f"Number of dangerous words: {dword_count}")
            save_content(content, is_html)
        
        else:
            print("Doesn't appear to be an HTML file with utf-8 encoding.")
            save_content(content, is_html)

if __name__ == "__main__":
    main()