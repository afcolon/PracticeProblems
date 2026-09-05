import {useState, useEffect, useCallback, useRef} from "react";
import { deleteSubscription, getSubscriptions, postSubscription, putSubscription } from "../api/subscriptions";
import Modal from "../components/Modal";
import ConfirmationModal from "../components/ConfirmationModal";
import styled from 'styled-components';
import { Pencil, Trash, CirclePlus } from 'lucide-react';

const GridDivContainer = styled.div`
    font-family: var(--font-body);
    color: var(--color-text-muted);
    width: 70%;
    padding-bottom: var(--space-md);
`;
const HeaderRow = styled.div`
    display: flex;
    padding: var(--space-sm) var(--space-md);
    font-size: 13px;
    color: var(--color-text-muted);
`;
const HeaderEmail = styled.span`
    flex: 1;
`;
const HeaderActions = styled.span`
    width: 72px;
    text-align: right;
`;
const Row = styled.div`
    background: var(--color-surface);
    border: 1px solid var(--color-border);
    border-radius: var(--radius-lg);
    padding: var(--space-md);
    display: flex;
    align-items: center;
    justify-content: space-between;

    &:hover {
        background: var(--color-surface-hover);
    }
`;
const RowList = styled.div`
    display: flex;
    flex-direction: column;
    gap: var(--space-sm);
`;
const IconButton = styled.button`
    width: 32px;
    height: 32px;
    padding: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: transparent;
    border: none;
    border-radius: var(--radius-sm);
    color: var(--color-text-muted);
    cursor: pointer;

    &:hover {
        background: var(--color-surface-hover);
        color: var(--color-text);
    }
`;
const ErrorDiv = styled.div`
    color: var(--color-error);
`;

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
        <GridDivContainer>
            {error &&
                <ErrorDiv>
                    Error: {error}
                    <button onClick={() => setError('')}>X</button>
                </ErrorDiv>
            }

        {loadError
            ? <button onClick={refreshGrid}>Retry fetch</button>
            : <div>
                <HeaderRow>
                    <HeaderEmail>Email</HeaderEmail>
                    <HeaderActions>Actions</HeaderActions>
                </HeaderRow>
                <RowList>
                    {subscriptions.map((sub) => (
                        <Row key={sub.id}>
                            <span>{sub.email}</span>
                            <div style={{display: 'flex', gap: '4px'}}>
                                <IconButton onClick={() => handleUpdate(sub)} title="Edit subscription"><Pencil /></IconButton>
                                <IconButton onClick={() => handleDelete(sub)} title="Delete subscription"><Trash /></IconButton>
                            </div>
                        </Row>
                    ))}
                </RowList>
            </div>
        }
        </GridDivContainer>
    )
}

const FormDiv = styled.div`
    display: flex;
    flex-direction: column;
    gap: var(--space-sm);
    margin-top: var(--space-md);
`

const StyledInput = styled.input`
    padding: var(--space-sm) var(--space-md);
    border-radius: var(--radius-sm);
    border: 1px solid var(--color-border);
    background: var(--color-bg);
    color: var(--color-text);
    font-family: var(--font-body);
    font-size: 14px;

    &:focus {
        outline: none;
        border-color: var(--color-accent);
    }
`;

const SubmitButton = styled.button`
    background: var(--color-accent);
    color: var(--color-accent-contrast);
    border: none;
    border-radius: var(--radius-sm);
    padding: var(--space-sm) var(--space-lg);
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;
    align-self: flex-start;

    &:hover {
        background: var(--color-accent-hover);
    }
`;

const ModalTitle = styled.p`
    font-family: var(--font-heading);
    color: var(--color-text);
    margin: 0 0 var(--space-md);
`

const ModalBody = styled.p`
    color: var(--color-error);
    font-size: 13px;
    margin: var(--space-sm) 0 0;
`

function SubscriptionModal ({showSubModal, setShowSubModal, refreshGrid, currentSub}) {
    const [email, setEmail] = useState(currentSub?.email || '');
    const [message, setMessage] = useState('');
    const createMode = !currentSub;

    const inputRef = useRef(null);

    useEffect(() => {
        if (showSubModal && inputRef.current) {
            inputRef.current.focus();
        }
    }, [showSubModal]);

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
                <ModalTitle>{createMode ? 'New subscription' : 'Edit subscription'}</ModalTitle>
                <form onSubmit={handleSubmit}>
                    <FormDiv>
                        <StyledInput
                            ref={inputRef}
                            type="email"
                            placeholder="Enter email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <SubmitButton type="submit">Submit</SubmitButton>
                    </FormDiv>
                </form>
            {message && <ModalBody>{message}</ModalBody>}
        </Modal>
    )
}

const PageDiv = styled.div`
    position: relative;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    align-items: center;

    &::before {
        content: '';
        position: absolute;
        top: -200px;
        left: 50%;
        transform: translateX(-50%);
        width: 600px;
        height: 400px;
        background: radial-gradient(circle, var(--color-accent) 0%, transparent 70%);
        opacity: 0.15;
        filter: blur(60px);
        pointer-events: none;
        z-index: 0;
    }

    & > * {
        position: relative;
        z-index: 1;
    }
`;
const PageHeader = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: var(--space-lg) 0;
`;
const PageTitle = styled.h1`
    font-family: var(--font-heading);
    color: var(--color-text);
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    padding: 0 var(--space-md);
`;

const PrimaryButton = styled.button`
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--color-accent);
    color: var(--color-accent-contrast);
    border: none;
    border-radius: var(--radius-sm);
    padding: var(--space-sm) var(--space-lg);
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;

    &:hover {
        background: var(--color-accent-hover);
    }
`;

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
        <PageDiv>
            <PageHeader>
                <PageTitle>Subscriptions</PageTitle>
                <PrimaryButton onClick={() => setShowNewSubModal(true)}>
                    <CirclePlus />New subscription
                </PrimaryButton>
            </PageHeader>

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
        </PageDiv>
    )
}