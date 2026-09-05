import Modal from './Modal';
import styled from 'styled-components';

const ModalTitle = styled.p`
    font-family: var(--font-heading);
    color: var(--color-text);
    margin: 0 0 var(--space-md);
`;

const ModalBody = styled.p`
    color: var(--color-text-muted);
    font-size: 14px;
    margin: var(--space-sm) 0 0;
`;

const ButtonDiv = styled.div`
    display: flex;
    gap: var(--space-md);
    margin-top: var(--space-lg);
`;

const ConfirmButton = styled.button`
    background: ${props => props.$isDestructive ? 'var(--color-error)' : 'var(--color-accent)'};
    color: var(--color-accent-contrast);
    border: none;
    border-radius: var(--radius-sm);
    padding: var(--space-sm) var(--space-lg);
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;

    &:hover {
        filter: brightness(0.8);
    }
`;

const CancelButton = styled.button`
    background: transparent;
    color: var(--color-text-muted);
    border: 1px solid var(--color-border);
    border-radius: var(--radius-sm);
    padding: var(--space-sm) var(--space-lg);
    font-size: 14px;
    font-weight: 600;
    cursor: pointer;

    &:hover {
        background: var(--color-surface-hover);
        color: var(--color-text);
    }
`;

export default function ConfirmationModal({ isOpen, onClose, onConfirm, header, message, isDestructive=false }) {
    return (
        <Modal 
            isOpen={isOpen}
            onClose={onClose}
        >
            <ModalTitle>{header}</ModalTitle>
            <ModalBody>{message}</ModalBody>
            <ButtonDiv>
                <ConfirmButton onClick={onConfirm} $isDestructive={isDestructive}>Confirm</ConfirmButton>
                <CancelButton onClick={onClose}>Cancel</CancelButton>
            </ButtonDiv>
        </Modal>
    );
};