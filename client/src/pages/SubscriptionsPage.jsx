import {useState, useEffect, useCallback} from "react";
import { deleteSubscription, getSubscriptions, postSubscription, putSubscription } from "../api/subscriptions";
import Modal from "../components/Modal";
import ConfirmationModal from "../components/ConfirmationModal";

function SubscriptionGrid ({ subscriptions, loadError, error, setError, setSelectedSub, setShowUpdateSubModal, setShowDeleteSubModal, refreshGrid }) {
    const handleUpdate = (sub) => {
        setSelectedSub(sub);
        setShowUpdateSubModal(true);
    }

    const handleDelete = (sub) => {
        setSelectedSub(sub);
        setShowDeleteSubModal(true);
    }
    
    return (
        <div>
            <h2>Subscriptions Grid</h2>
            {error &&
                <div>
                    Error: {error}
                    <button onClick={() => setError('')}>X</button>
                </div>
            }

        {loadError
            ? <button onClick={refreshGrid}>Retry fetch</button>
            : <table>
                <tbody>
                    {subscriptions.map((sub) => (
                        <tr key={sub.id}>
                            <td>{sub.email}</td>
                            <td>
                                <button onClick={() => handleUpdate(sub)}>Update</button>
                            </td>
                            <td>
                                <button onClick={() => handleDelete(sub)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        }
        </div>
    )
}

function SubscriptionModal ({showSubModal, setShowSubModal, refreshGrid, currentSub}) {
    const [email, setEmail] = useState(currentSub?.email || '');
    const [message, setMessage] = useState('');
    const createMode = !currentSub;

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            const response = createMode
                ? await postSubscription(email)
                : await putSubscription(currentSub.id, email);

            if (response.success) {
                refreshGrid();
                setShowSubModal(false);
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
        setShowSubModal(false)
    }

    return (
        <Modal isOpen={showSubModal} onClose={() => handleClose()}>
            <h3>
                {createMode
                    ? 'New subscription'
                    : 'Edit subscription'
                }
            </h3>
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
    const [loadError, setLoadError] = useState('');
    const [subscriptions, setSubscriptions] = useState([]);
    const [selectedSub, setSelectedSub] = useState({});

    const [showNewSubModal, setShowNewSubModal] = useState(false);
    const [showUpdateSubModal, setShowUpdateSubModal] = useState(false);
    const [showDeleteSubModal, setShowDeleteSubModal] = useState(false);

    const fetchData = useCallback(async () => {
            setLoading(true);
            try {
                const data = await getSubscriptions();
                setSubscriptions(data);
                setLoadError('');
            } catch(err) {
                setLoadError(err.message);
            } finally {
                setLoading(false);
            }
    }, []);

    useEffect(() => { 
        fetchData();
    }, [fetchData]);

    const handleRefresh = () => { fetchData(); }

    const handleDelete = async () => {
        try {
            const { success, message } = await deleteSubscription(selectedSub.id);
            if (success) {
                handleRefresh();
            } else {
                setError(message)
            }
        } catch(err) {
            setError(err.message);
        } finally {
            setShowDeleteSubModal(false);
        }
    };

    return (
        <div>
            <div>
                <h1>Subscription page</h1>
                <button onClick={() => setShowNewSubModal(true)}>
                    New subscription
                </button>
            </div>

            {loading
                ? <div>Loading</div>
                : <SubscriptionGrid
                    subscriptions={subscriptions}
                    loadError={loadError}
                    error={error}
                    setError={setError}
                    setSelectedSub={setSelectedSub}
                    setShowUpdateSubModal={setShowUpdateSubModal}
                    setShowDeleteSubModal={setShowDeleteSubModal}
                    refreshGrid={handleRefresh}
                />
            }

            {showNewSubModal &&
                <SubscriptionModal
                    showSubModal={showNewSubModal}
                    setShowSubModal={setShowNewSubModal}
                    refreshGrid={handleRefresh}
                />
            }
            {showUpdateSubModal &&
                <SubscriptionModal
                    showSubModal={showUpdateSubModal}
                    setShowSubModal={setShowUpdateSubModal}
                    refreshGrid={handleRefresh}
                    currentSub={selectedSub}
                />
            }
            {showDeleteSubModal &&
                <ConfirmationModal
                    isOpen={showDeleteSubModal}
                    onClose={() => setShowDeleteSubModal(false)}
                    onConfirm={handleDelete}
                    header={'Delete subscription?'}
                    message={'Are you sure you want to delete this subscription: ' + selectedSub.email}
                />
            }
        </div>
    )
}