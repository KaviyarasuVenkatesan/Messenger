import axios from 'axios';
import debounce from 'lodash/debounce';
import React, { useState, useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import { jwtDecode } from 'jwt-decode';
import '../messenger.css';

const API_BASE_URL = 'http://localhost:8080';

const Messenger = () =>
{
    const [username, setUsername] = useState('');
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [sendingRequest, setSendingRequest] = useState({});
    const [loggedInUserId, setLoggedInUserId] = useState(null);
    const [pendingRequests, setPendingRequests] = useState([]);
    const [showNotifications, setShowNotifications] = useState(false);
    const [loggedInUserName, setLoggedInUserName] = useState(null);
    const [friends, setFriends] = useState([]);
    const [selectedFriend, setSelectedFriend] = useState(null);
    const [messages, setMessages] = useState([]);
    const [messageInput, setMessageInput] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const [unreadMessages, setUnreadMessages] = useState({});


    const messagesEndRef = useRef(null);
    const selectedFriendRef = useRef(null);


    useEffect(() =>
    {
        if (loggedInUserName && loggedInUserId)
        {
            const socket = new SockJS(`${API_BASE_URL}/ws`);
            const client = over(socket);

            client.connect({}, () =>
            {
                onConnect(client);
            });

            fetchFriends(loggedInUserId);

            setStompClient(client);

            return () =>
            {
                if (client !== null)
                {
                    client.disconnect(() =>
                    {
                        console.log('Disconnected');
                    });
                }
            };
        }
    }, [loggedInUserName, loggedInUserId]);

    const onConnect = (client) =>
    {
        console.log(`/user/${loggedInUserName}/private`)
        client.subscribe(`/user/${loggedInUserName}/private`, onPrivateMessageReceived)
        client.subscribe(`/user/${loggedInUserName}/friends`, onAddingFriends)
        client.subscribe(`/user/${loggedInUserName}/getNotification`, onNotificationAdded)
        client.subscribe(`/user/${loggedInUserName}/updateNotification`, onNotificationUpdate)
    };
        

    useEffect(() =>
    {
        const token = sessionStorage.getItem('token');
        if (token)
        {
            const decodedToken = jwtDecode(token);
            setLoggedInUserName(decodedToken.sub);
            setLoggedInUserId(decodedToken.id);
        }
    }, []);

    useEffect(() =>
    {
        const debouncedSearch = debounce(async (query) =>
        {
            if (query)
            {
                try
                {
                    const response = await axios.get(`${API_BASE_URL}/search`, {
                        params: {
                            userName: query,
                            senderId: loggedInUserId
                        }
                    });
                    setResults(response.data);
                } catch (error)
                {
                    console.error('Error searching users:', error);
                }
            } else
            {
                setResults([]);
            }
        }, 300);

        debouncedSearch(username);

        return () =>
        {
            debouncedSearch.cancel();
        };
    }, [username]);

    const handleSearchInputFocus = () =>
    {
        setShowNotifications(false);
    };

    useEffect(() =>
    {
        const fetchNotification = async () =>
        {
            try
            {
                const response = await axios.get(`${API_BASE_URL}/pending`, {
                    params: {
                        userId: loggedInUserId
                    }
                });
                console.log(response.data)
                setPendingRequests(response.data);
            } catch (error)
            {
                console.error('Error fetching pending requests:', error);
            }
        };

        if (loggedInUserId)
        {
            fetchNotification()
        }
    }, [loggedInUserId])


    const handleSendRequest = async (receiverId) =>
    {
        try
        {
            const response = await axios.post(`${API_BASE_URL}/friend-request/send`, null, {
                params: {
                    senderId: loggedInUserId,
                    receiverId: receiverId
                }
            });
            alert('Friend request sent successfully!');
        } catch (error)
        {
            if (error.response && error.response.status === 409)
            {
                alert('Friend request already sent.');
            } else
            {
                console.error('Error sending friend request:', error);
                alert('An error occurred while sending the friend request.');
            }
        }
    };

    const handleCancelRequest = async (receiverId) =>
    {
        try
        {
            const response = await axios.post(`${API_BASE_URL}/friend-request/cancel`, null, {
                params: {
                    senderId: loggedInUserId,
                    receiverId: receiverId
                }
            });
            console.log('Request Cancelled Successfully');
        } catch (error)
        {
            console.error('Error cancelling friend request:', error);
        }
    };

    const handleRequestRejected = async (senderId) =>
    {
        try
        {
            const response = await axios.post(`${API_BASE_URL}/friend-request/reject`, null, {
                params: {
                    senderId: senderId,
                    receiverId: loggedInUserId
                }
            });
            console.log('Request Rejected Successfully')
            setPendingRequests((prevRequests) => 
                prevRequests.filter((request) => request.sender.id !== senderId)
            )
        }catch(error)
        { console.log('Error in Rejecting the Request : ', error)}
    }

    const handleRequestAccept = async (sender) =>
    {
        console.log(sender)
        try
        {
            const response = await axios.post(`${API_BASE_URL}/friend-request/accept`, null, {
                params: {
                    senderId: sender.id,
                    receiverId: loggedInUserId
                }
            })
            console.log("Request Accepted")
            addFriend(sender)
            setPendingRequests((prevRequests) =>
                prevRequests.filter((request) => request.sender.id !== sender.id)
            );
        } catch (error)
        {
            console.error("Error in accept:", error)
        }
    }

    const fetchFriends = async (userId) =>
    {
        try
        {
            const response = await axios.get(`${API_BASE_URL}/friends`, {
                params: { userId: userId }
            });
            setFriends(response.data.friends);
        } catch (error)
        {
            console.error("Error fetching friends: " + error);
        }
    };

    const fetchMessages = async (friendId) =>
    {
        try
        {
            const response = await axios.get(`${API_BASE_URL}/messages`, {
                params: {
                    userId: loggedInUserId,
                    friendId: friendId
                }
            });
            setMessages(response.data);
        } catch (error)
        {
            console.error("Error fetching messages: " + error);
        }
    };

    const addFriend = (friend) =>
    {
        setFriends((prevFriends) =>
        {
            if (!prevFriends.some((pf) => pf.id === friend.id))
            {
                return [...prevFriends, friend]
            }
            return prevFriends
        }
        )
    }

    const handleSendMessage = () =>
    {
        if (messageInput.trim() !== '' && stompClient && selectedFriend)
        {
            const message = {
                sender: { id: loggedInUserId },
                receiver: selectedFriend,
                content: messageInput,
                timestamp: new Date()
            };
            console.log(selectedFriend)
            stompClient.send('/app/private-message', {}, JSON.stringify(message));
            setMessages((prevMessages) => [...prevMessages, message]);
            setMessageInput('');
        }
    };
    
    const handleFriendClick = (friend) =>
    {
        console.log('Friend clicked:'+ friend.userName);
        setSelectedFriend(friend);
        selectedFriendRef.current = friend;
        fetchMessages(friend.id);
    
        // Reset unread messages count for the selected friend
        setUnreadMessages((prevUnreadMessages) => ({
            ...prevUnreadMessages,
            [friend.id]: 0
        }));
    };

    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollTop = messagesEndRef.current.scrollHeight;
        }
    }, [messages]);
        
    
    useEffect(() =>
    {
        if (selectedFriend)
        {
            fetchMessages(selectedFriend.id);
        }
    }, [selectedFriend]);

    const handleNotificationClick = () =>
    {
        setShowNotifications(!showNotifications);
    };

    // Call back functions 

    const onPrivateMessageReceived = (message) =>
    {
        console.log("message from private message received for static" + message);
        const parsedMessage = JSON.parse(message.body);
        console.log("parsed Message : " + parsedMessage.sender.id)
        console.log("selected Friend : ", selectedFriendRef.current)
        if (selectedFriendRef.current && selectedFriendRef.current.id === parsedMessage.sender.id)
        {
            setMessages((prevMessages) => [...prevMessages, parsedMessage]);
        }
        else if(selectedFriendRef.current && !(selectedFriendRef.current.id === parsedMessage.sender.id) )
        {
            setUnreadMessages((prevUnreadMessages) => ({
                ...prevUnreadMessages,
                [parsedMessage.sender.id]: (prevUnreadMessages[parsedMessage.sender.id] || 0) + 1
            }));
        }else
        {
            setUnreadMessages((prevUnreadMessages) => ({
                ...prevUnreadMessages,
                [parsedMessage.sender.id]: (prevUnreadMessages[parsedMessage.sender.id] || 0) + 1
            }));
        }
    };

    const onAddingFriends = (friendFromServer) =>
    {
        const friend = JSON.parse(friendFromServer.body)
        console.log(friend)
        addFriend(friend)
    }

    const onNotificationAdded = (newRequest) => {
        const request = JSON.parse(newRequest.body)
        setPendingRequests((prevPendingRequest) => {
            if(!prevPendingRequest.some((ppr) => ppr.id === request.id))
            {
                return [...prevPendingRequest, request]
            }
            return prevPendingRequest
        })
    }

    const onNotificationUpdate = (senderFromServer) => {
        const sender = JSON.parse(senderFromServer.body)
        setPendingRequests((prevRequests) =>
            prevRequests.filter((request) => request.sender.id !== sender.id)
        );
    }

    return (
        <div className="search-container">
            <div className="header">
                <input
                    type="text"
                    placeholder="Search users by username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    onFocus={handleSearchInputFocus}
                />
                <button className="notification-button" onClick={handleNotificationClick}>
                    Notifications {pendingRequests.length > 0 && `(${pendingRequests.length})`}
                </button>
            </div>
            {showNotifications && (
                <div className="results">
                    <h3>Pending Friend Requests</h3>
                    {pendingRequests.length === 0 ? (
                        <p>No pending requests.</p>
                    ) : (
                        <ul>
                            {pendingRequests.map((request) => (
                                <li key={request.id} className='result-item'>
                                    {request.sender.userName}
                                    <button
                                        id="send-button"
                                        onClick={() => handleRequestAccept(request.sender)}
                                    >
                                        Accept
                                    </button>
                                    <button
                                        id="send-button"
                                        onClick={() => handleRequestRejected(request.sender.id)}
                                    >
                                        Reject
                                    </button>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            )}
            <div className="results">
                {results.map((user) => (
                    <div key={user.id} className="result-item">
                        <span>{user.userName}</span>
                        {user.requestStatus !== 'ACCEPTED' &&
                            <>
                                <button
                                    id="send-button"
                                    onClick={() => handleSendRequest(user.id)}
                                    disabled={sendingRequest[user.id]}
                                    className={sendingRequest[user.id] ? 'disabled' : ''}
                                >
                                    <span className="icon">🔗</span>
                                    Send Request
                                    {sendingRequest[user.id] && <span className="spinner"></span>}
                                </button>
                                <button
                                    id="cancel-button"
                                    onClick={() => handleCancelRequest(user.id)}
                                >
                                    <span className="icon">❌</span>
                                    Cancel Request
                                </button>
                            </>
                        }
                        {user.requestStatus === 'ACCEPTED' &&
                            <button
                                id='send-button'
                                onClick={() => handleFriendClick(user)}
                            >
                                Send Message
                            </button>
                        }

                    </div>
                ))}
            </div>
            <div className="main-content">
                <div className="friends-list">
                    <h2>Friends List</h2>
                    <ul>
                        {friends.length > 0 ? (
                            friends.map((friend) => (
                                <li key={friend.id} onClick={() => handleFriendClick(friend)}>
                                    {friend.userName}
                                    {unreadMessages[friend.id] > 0 && (
                                        <span className="unread-notification">
                                            {unreadMessages[friend.id]}
                                        </span>
                                    )}
                                </li>
                            ))
                        ) : (
                            <li>No friends found</li>
                        )}
                    </ul>
                </div>
                <div className="message-content">
                    {selectedFriend && (
                        <>
                            <h2>Chat with {selectedFriend.userName}</h2>
                                <div className="messages" ref={messagesEndRef} >
                                {messages.map((msg, index) => (
                                    <div key={index} className={msg.sender.id === loggedInUserId ? 'my-message' : 'their-message'}>
                                        <span>{msg.content}</span>
                                    </div>
                                ))}
                            </div>
                            <div className="input-area">
                                <input
                                    type="text"
                                    value={messageInput}
                                    onChange={(e) => setMessageInput(e.target.value)}
                                    placeholder="Type a message"
                                />
                                <button onClick={handleSendMessage}>Send</button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </div>
    );

};

export default Messenger;
