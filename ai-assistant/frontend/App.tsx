import {useEffect, useState} from "react";
import {AssistantService} from "Frontend/generated/endpoints";
import {GridColumn} from "@hilla/react-components/GridColumn";
import {Grid} from "@hilla/react-components/Grid";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {MessageInput} from "@hilla/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@hilla/react-components/SplitLayout";

const chatId = nanoid();
export default function App() {
    const [working, setWorking] = useState(false);
    const [messages, setMessages] = useState<MessageListItem[]>([{
        userName: 'Assistant',
        text: 'Welcome to JPHealth! How can I help you?',
        userColorIndex: 1
    }]);

    function addMessage(message: MessageListItem) {
        setMessages(messages => [...messages, message]);
    }

    function appendToLastMessage(chunk: string) {
        setMessages(messages => {
            const lastMessage = messages[messages.length - 1];
            lastMessage.text += chunk;
            return [...messages.slice(0, -1), lastMessage];
        });
    }

    async function sendMessage(message: string) {
        setWorking(true);
        addMessage({
            text: message,
            userName: 'You',
            userColorIndex: 2
        });
        let first = true;
        AssistantService.chat(chatId, message)
            .onNext(chunk => {
                if (first && chunk) {
                    addMessage({
                        text: chunk,
                        userName: 'Assistant',
                        userColorIndex: 1
                    });

                    first = false;
                } else {
                    appendToLastMessage(chunk);
                }
            })
            .onError(() => setWorking(false))
            .onComplete(() => setWorking(false));
    }

    return (
        <SplitLayout className="h-full">
            <div className="flex flex-col gap-m p-m box-border h-full" style={{width: '100%'}}>
                <h3>JPHealth customer support 🧑🏻‍</h3>
                <MessageList items={messages} className="flex-grow"/>
                <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
            </div>
         </SplitLayout>
    );
}
