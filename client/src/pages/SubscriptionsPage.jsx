import {useState, useEffect, useCallback} from "react";
import { getSubscriptions, postSubscription } from "../api/subscriptions";
import Modal from "../components/Modal";

function SubscriptionGrid ({ subscriptions }) {
    return (
        <div>
            <h2>Subscriptions Grid</h2>
            <table>
                <tbody>
                    {subscriptions.map((sub) => (
                        <tr key={sub.id}>
                            <td>{sub.email}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
        
    )
}

function NewSubscriptionModal ({showNewSubModal, setShowNewSubModal, refreshGrid}) {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            const response = await postSubscription(email);

            if (response.success) {
                refreshGrid();
                setShowNewSubModal(false);
            } else {
                setMessage(response.message);
            }
        } catch(err) {
            setMessage(err.message);
        }
    }

    const handleClose = () => {
        setEmail('');
        setMessage('');
        setShowNewSubModal(false)
    }

    return (
        <Modal isOpen={showNewSubModal} onClose={() => handleClose()}>
            <h3>modal view</h3>
            <div>{message}</div>

            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Enter email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <button type="submit">Submit</button>
            </form>

        </Modal>
    )
}


export default function SubscriptionsPage() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [subscriptions, setSubscriptions] = useState([]);

    const [showNewSubModal, setShowNewSubModal] = useState(false);

    const fetchData = useCallback(async () => {
            setLoading(true);
            try {
                const data = await getSubscriptions();
                setSubscriptions(data);
            } catch(err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
    }, []);

    useEffect(() => { 
        fetchData();
    }, [fetchData]);

    const handleRefresh = () => { fetchData(); }

    return (
        <div>
            <div>
                <h1>Subscription page</h1>
                <button onClick={() => setShowNewSubModal(true)}>
                    New subscription
                </button>
            </div>

            {error
                ? error
                : loading
                    ? <div>Loading</div>
                    : <SubscriptionGrid subscriptions={subscriptions} />
            }

            {showNewSubModal &&
                <NewSubscriptionModal
                    showNewSubModal={showNewSubModal}
                    setShowNewSubModal={setShowNewSubModal}
                    refreshGrid={handleRefresh}
                />
            }
        </div>
    )
}