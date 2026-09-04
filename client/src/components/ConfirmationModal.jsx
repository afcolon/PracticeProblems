import Modal from './Modal';

export default function ConfirmationModal({ isOpen, onClose, onConfirm, header, message }) {
    return (
        <Modal 
            isOpen={isOpen}
            onClose={onClose}
        >
            <h3>{header}</h3>
            <p>{message}</p>
            <button onClick={onConfirm}>Confirm</button>
            <button onClick={onClose}>Cancel</button>
        </Modal>
    );
};